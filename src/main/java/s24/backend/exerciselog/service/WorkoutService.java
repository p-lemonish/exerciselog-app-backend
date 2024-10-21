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
    @Autowired
    private CompletedWorkoutMapper completedWorkoutMapper;

    @Autowired
    private PlannedExerciseLogMapper plannedExerciseLogMapper;

    @Transactional
    public void getAllAttributes(Model model) { //TODO addattribute DTO's
        User user = SecurityUtils.getCurrentUser();
        List<WorkoutDto> workoutsDto = workoutMapper.toWorkoutDtos(workoutRepository.findByUser(user));
        List<CompletedWorkoutDto> completedWorkoutDtos = completedWorkoutMapper.toCompletedWorkoutDtos(completedWorkoutRepository.findByUser(user));
        List<PlannedExerciseLogDto> plannedExerciseLogDtos = plannedExerciseLogMapper.toDtos(plannedExerciseLogRepository.findByUser(user));
        model.addAttribute("workouts", workoutsDto);
        model.addAttribute("completedWorkouts", completedWorkoutDtos);
        model.addAttribute("plannedExercises", plannedExerciseLogDtos);
    }
    @Transactional
    public void startWorkout(Long workoutId, Model model) {
        Workout workout = workoutRepository.findById(workoutId).orElseThrow(() -> new RuntimeException("Workout not found"));
        WorkoutDto workoutDto = workoutMapper.toWorkoutDto(workout);

        //Initialize exercises within workout
        List<ExerciseLogDto> exercises = exerciseLogMapper.toExerciseLogDtoListFromPlannedExerciseLogs(workout.getPlannedExerciseLogs());
        workoutDto.setExercises(exercises);
        model.addAttribute("workoutDto", workoutDto);
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
        for(ExerciseLog log : workout.getExerciseLog()) {
            log.setWorkout(null);
            exerciseLogRepository.save(log);
        }
        workoutRepository.delete(workout);
    }

    /* Perhaps dont let it happen
    @Transactional
    public void deleteCompletedWorkout(Long workoutId) {
        CompletedWorkout completedWorkout = completedWorkoutRepository.findById(workoutId).orElseThrow(() -> new RuntimeException("Completed workout not found"));
        for(ExerciseLog log : completedWorkout.getExerciseLogs()) {
            log.setWorkout(null);
            exerciseLogRepository.save(log);
        }
        completedWorkoutRepository.delete(completedWorkout);
    }
     */
    @Transactional
    public void completeWorkout(Long workoutId, WorkoutDto workoutDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        Workout workout = workoutRepository.findById(workoutId).orElseThrow(() -> new RuntimeException("Workout not found"));
        CompletedWorkout completedWorkout = workoutMapper.toCompletedWorkout(workoutDto, workout, currentUser);
        completedWorkoutRepository.save(completedWorkout);

        //Fill to-be-completed workout's ExerciseLog with data
        for(ExerciseLogDto exerciseData : workoutDto.getExercises()) {
            PlannedExerciseLog plannedExerciseLog = plannedExerciseLogRepository.findById(exerciseData.getExerciseId())
                .orElseThrow(() -> new RuntimeException("Planned Exercise not found"));
            ExerciseLog exerciseLog = exerciseLogMapper.toExerciseLog(exerciseData, plannedExerciseLog, workout, currentUser, completedWorkout);

            // Fill SetLog-list with data
            List<SetLogDto> setDatas = exerciseData.getSetData();
            List<SetLog> setLogs = new ArrayList<>();
            for (SetLogDto setData : setDatas) {
                SetLog setLog = exerciseLogMapper.toSetLog(setData, exerciseLog);
                setLogs.add(setLog);
            }
            exerciseLog.setSetLogs(setLogs);
            exerciseLogRepository.save(exerciseLog);
        }
        //user.getWorkouts().remove(workout);
    }
}
