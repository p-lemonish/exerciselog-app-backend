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
        List<WorkoutDto> workoutsDto = workoutMapper.toDtoList(workoutRepository.findByUser(user));
        List<CompletedWorkoutDto> completedWorkoutDtos = completedWorkoutMapper.toDtoList(completedWorkoutRepository.findByUser(user));
        List<PlannedExerciseLogDto> plannedExerciseLogDtos = plannedExerciseLogMapper.toDtoList(plannedExerciseLogRepository.findByUser(user));
        model.addAttribute("workouts", workoutsDto);
        model.addAttribute("completedWorkouts", completedWorkoutDtos);
        model.addAttribute("plannedExercises", plannedExerciseLogDtos);
    }
    @Transactional
    public void startWorkout(Long workoutId, Model model) {
        Workout workout = workoutRepository.findById(workoutId).orElseThrow(() -> new RuntimeException("Workout not found"));
        WorkoutDto workoutDto = workoutMapper.toDto(workout);

        //Initialize exercises within workout
        List<ExerciseLogDto> exercises = exerciseLogMapper.plannedExerciseLogListToDtoList(workout.getPlannedExerciseLogs());
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
        for(ExerciseLog log : workout.getExerciseLogs()) {
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
    public void completeWorkout(Long workoutId, CompletedWorkoutDto completedWorkoutDto) {
    User currentUser = SecurityUtils.getCurrentUser();
    Workout workout = workoutRepository.findById(workoutId)
        .orElseThrow(() -> new RuntimeException("Workout not found"));
    CompletedWorkout completedWorkout = completedWorkoutMapper.toEntity(completedWorkoutDto);
    completedWorkout.setUser(currentUser);
    completedWorkout.setDate(LocalDate.now());
    completedWorkout.setWorkoutName(workout.getName());
    completedWorkout.setPlannedDate(workout.getDate());
    completedWorkoutRepository.save(completedWorkout);

    for (ExerciseLogDto exerciseDto : completedWorkoutDto.getExercises()) {
        PlannedExerciseLog plannedExerciseLog = plannedExerciseLogRepository.findById(exerciseDto.getExerciseId())
            .orElseThrow(() -> new RuntimeException("Planned Exercise not found"));
        ExerciseLog exerciseLog = exerciseLogMapper.toEntity(
            exerciseDto,
            new ExerciseLog(),
            plannedExerciseLog,
            currentUser,
            workout,
            completedWorkout
        );
        exerciseLog.setCompletedWorkout(completedWorkout);
        exerciseLogRepository.save(exerciseLog);
    }
}

}
