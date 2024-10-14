package s24.backend.exerciselog.service;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jakarta.transaction.Transactional;
import s24.backend.exerciselog.domain.*;
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
    
    @Transactional
    public void completeWorkout(Long workoutId, String notes) {
        User currentUser = SecurityUtils.getCurrentUser();
        Workout workout = workoutRepository.findById(workoutId).orElseThrow(() -> new RuntimeException("Workout not found"));

        CompletedWorkout completedWorkout = new CompletedWorkout();
        LocalDate date = LocalDate.now();
        completedWorkout.setUser(currentUser);
        completedWorkout.setDate(date);
        completedWorkout.setNotes(notes);
        completedWorkout.setWorkoutName(workout.getName());
        completedWorkout.setWorkoutNotes(workout.getNotes());
        completedWorkout.setPlannedDate(workout.getDate());
        completedWorkoutRepository.save(completedWorkout);

        for(PlannedExerciseLog plannedExerciseLog : workout.getPlannedExerciseLogs()) {
            ExerciseLog exerciseLog = new ExerciseLog();
            exerciseLog.setUser(currentUser);
            exerciseLog.setCompletedWorkout(completedWorkout);
            exerciseLog.setExercise(plannedExerciseLog.getExercise());
            exerciseLog.setWorkout(workout);
            exerciseLog.setName(plannedExerciseLog.getExercise().getName());
            exerciseLog.setPlannedExerciseLog(plannedExerciseLog);
            exerciseLog.setNotes(notes);
            // TODO SetLog
            exerciseLogRepository.save(exerciseLog);
        }
        //user.getWorkouts().remove(workout);
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
    public void startWorkout(Long workoutId, Model model) {
        Workout workout = workoutRepository.findById(workoutId).orElseThrow(() -> new RuntimeException("Workout not found"));
        model.addAttribute("workout", workout);
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
    public void getAllAttributes(Model model) {
        model.addAttribute("workouts", workoutRepository.findAll());
        model.addAttribute("completedWorkouts", completedWorkoutRepository.findAll());
        model.addAttribute("plannedExercises", plannedExerciseLogRepository.findAll());
    }
}
