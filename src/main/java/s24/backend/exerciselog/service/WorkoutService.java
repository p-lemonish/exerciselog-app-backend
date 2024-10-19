package s24.backend.exerciselog.service;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jakarta.transaction.Transactional;
import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.dto.*;
import s24.backend.exerciselog.mapper.*;
import s24.backend.exerciselog.repository.*;
import s24.backend.exerciselog.util.SecurityUtils;

@Service
public class WorkoutService {
    @Autowired
    private WorkoutRepository workoutRepository;
    @Autowired
    private CompletedWorkoutRepository completedWorkoutRepository;
    @Autowired
    private ExerciseLogRepository exerciseLogRepository;
    @Autowired
    private PlannedExerciseLogRepository plannedExerciseLogRepository;
    @Autowired
    private ExerciseLogMapper exerciseLogMapper;
    @Autowired
    private WorkoutMapper workoutMapper;

    @Transactional
    public void getAllAttributes(Model model) { //TODO findAll -> findByUser
        model.addAttribute("workouts", workoutRepository.findAll());
        model.addAttribute("completedWorkouts", completedWorkoutRepository.findAll());
        model.addAttribute("plannedExercises", plannedExerciseLogRepository.findAll());
    }
    @Transactional
    public void startWorkout(Long workoutId, Model model) {
        Workout workout = workoutRepository.findById(workoutId).orElseThrow(() -> new RuntimeException("Workout not found"));
        model.addAttribute("workout", workout);

        WorkoutCompletionForm workoutCompletionForm = new WorkoutCompletionForm();

        //Initialize exercises within workout
        List<ExerciseCompletionData> exercises = new ArrayList<>();
        for(PlannedExerciseLog plannedExerciseLog : workout.getPlannedExerciseLogs()) {
            ExerciseCompletionData exerciseData = new ExerciseCompletionData();
            exerciseData.setExerciseId(plannedExerciseLog.getId());
            exerciseData.setExerciseName(plannedExerciseLog.getExercise().getName());

            //Initialize individual set data
            List <SetData> sets = new ArrayList<>();
            for(int i = 1; i <= plannedExerciseLog.getPlannedSets(); i++) {
                SetData setData = new SetData();
                setData.setSetNumber(i);
                setData.setReps(plannedExerciseLog.getPlannedReps());
                setData.setWeight(plannedExerciseLog.getPlannedWeight());
                sets.add(setData);
            }
            exerciseData.setSetData(sets);

            exercises.add(exerciseData);
        }
        workoutCompletionForm.setExercises(exercises);
        model.addAttribute("workoutCompletionForm", workoutCompletionForm);
    }
    @Transactional
    public void addWorkout(String name, LocalDate date, String notes, List<Long> plannedExerciseIds) {
        User user = SecurityUtils.getCurrentUser();

        List<PlannedExerciseLog> selectedPlannedExercises = new ArrayList<>();
        if(plannedExerciseIds != null && !plannedExerciseIds.isEmpty()) {
            selectedPlannedExercises = plannedExerciseLogRepository.findAllById(plannedExerciseIds);
        }
        
        Workout newWorkout = new Workout(user, selectedPlannedExercises, name, notes, date);
        workoutRepository.save(newWorkout);
    }
    @Transactional
    public void deletePlannedWorkout(Long workoutId) {
        Workout workout = workoutRepository.findById(workoutId).orElseThrow(() -> new RuntimeException("Workout not found"));
        workoutRepository.delete(workout);
    }

    @Transactional
    public void deleteCompletedWorkout(Long workoutId) {
        CompletedWorkout completedWorkout = completedWorkoutRepository.findById(workoutId).orElseThrow(() -> new RuntimeException("Completed workout not found"));
        completedWorkoutRepository.delete(completedWorkout);
    }
    
    @Transactional
    public void completeWorkout(Long workoutId, WorkoutCompletionForm workoutCompletionForm) {
        User currentUser = SecurityUtils.getCurrentUser();
        Workout workout = workoutRepository.findById(workoutId).orElseThrow(() -> new RuntimeException("Workout not found"));
        CompletedWorkout completedWorkout = workoutMapper.toCompletedWorkout(workoutCompletionForm, workout, currentUser);
        completedWorkoutRepository.save(completedWorkout);

        //Fill to-be-completed workout's ExerciseLog with data
        for(ExerciseCompletionData exerciseData : workoutCompletionForm.getExercises()) {
            PlannedExerciseLog plannedExerciseLog = plannedExerciseLogRepository.findById(exerciseData.getExerciseId())
                .orElseThrow(() -> new RuntimeException("Planned Exercise not found"));
            ExerciseLog exerciseLog = exerciseLogMapper.toExerciseLog(exerciseData, plannedExerciseLog, workout, currentUser, completedWorkout);

            // Fill SetLog-list with data
            List<SetData> setDatas = exerciseData.getSetData();
            List<SetLog> setLogs = new ArrayList<>();
            for (SetData setData : setDatas) {
                SetLog setLog = exerciseLogMapper.toSetLog(setData, exerciseLog);
                setLogs.add(setLog);
            }
            exerciseLog.setSetLogs(setLogs);
            exerciseLogRepository.save(exerciseLog);
        }
        //user.getWorkouts().remove(workout);
    }
}
