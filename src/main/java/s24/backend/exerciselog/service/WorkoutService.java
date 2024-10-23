package s24.backend.exerciselog.service;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import jakarta.transaction.Transactional;
import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.dto.*;
import s24.backend.exerciselog.exception.ResourceNotFoundException;
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
    public List<WorkoutDto> getUserWorkouts(User user) {
        List<Workout> workouts = workoutRepository.findByUser(user);
        return workoutMapper.toDtoList(workouts);
    }
    @Transactional
    public List<CompletedWorkoutDto> getUserCompletedWorkouts(User user) {
        List<CompletedWorkout> completedWorkouts = completedWorkoutRepository.findByUser(user);
        return completedWorkoutMapper.toDtoList(completedWorkouts);
    }
    @Transactional
    public List<PlannedExerciseLogDto> getUserPlannedExercises(User user) {
        List<PlannedExerciseLog> plannedExerciseLogs = plannedExerciseLogRepository.findByUser(user);
        return plannedExerciseLogMapper.toDtoList(plannedExerciseLogs);
    }
    @Transactional
    public void addWorkout(WorkoutDto workoutDto, User user, BindingResult result) {

        List<Long> selectedExerciseIds = workoutDto.getSelectedExerciseIds();

        if(selectedExerciseIds == null || selectedExerciseIds.isEmpty()) {
            result.rejectValue("selectedExerciseIds", "error.workoutDto", "Please select at least one exercise");
            return;
        }

        List<PlannedExerciseLog> selectedPlannedExerciseLogs = plannedExerciseLogRepository.findAllById(selectedExerciseIds);

        Workout workout = workoutMapper.toEntity(workoutDto, user);
        workout.setPlannedExerciseLogs(selectedPlannedExerciseLogs);
        workoutRepository.save(workout);
    }

    @Transactional
    public void completeWorkout(Long workoutId, CompletedWorkoutDto completedWorkoutDto, BindingResult result) {
        User currentUser = SecurityUtils.getCurrentUser();
        Workout workout = workoutRepository.findById(workoutId)
            .orElseThrow(() -> new ResourceNotFoundException("Workout not found"));
        
        LocalDate now = LocalDate.now();
        CompletedWorkout completedWorkout = completedWorkoutMapper.toEntity(completedWorkoutDto);
        completedWorkout.setUser(currentUser);
        completedWorkout.setDate(now);
        completedWorkout.setWorkoutName(workout.getName());
        completedWorkout.setPlannedDate(workout.getDate());
        completedWorkoutRepository.save(completedWorkout);

        for (ExerciseLogDto exerciseDto : completedWorkoutDto.getExercises()) {
            PlannedExerciseLog plannedExerciseLog = plannedExerciseLogRepository.findById(exerciseDto.getExerciseId())
                .orElseThrow(() -> new ResourceNotFoundException("Planned Exercise not found"));
            ExerciseLog exerciseLog = exerciseLogMapper.toEntity(
                exerciseDto,
                new ExerciseLog(),
                plannedExerciseLog,
                currentUser,
                workout
            );
            exerciseLog.setDate(now);
            exerciseLogRepository.save(exerciseLog);
        }
    }
    @Transactional
    public CompletedWorkoutDto startWorkout(Long workoutId) {
        Workout workout = workoutRepository.findById(workoutId).orElseThrow(() -> new ResourceNotFoundException("Workout not found"));

        CompletedWorkoutDto completedWorkoutDto = new CompletedWorkoutDto();
        completedWorkoutDto.setWorkoutName(workout.getName());
        completedWorkoutDto.setPlannedDate(workout.getDate());
        completedWorkoutDto.setId(workoutId);

        //Initialize exercises within workout
        List<ExerciseLogDto> exercises = exerciseLogMapper.plannedExerciseLogListToDtoList(workout.getPlannedExerciseLogs());
        completedWorkoutDto.setExercises(exercises);

        return completedWorkoutDto;
    }
    @Transactional
    public void deletePlannedWorkout(Long workoutId) {
        Workout workout = workoutRepository.findById(workoutId).orElseThrow(() -> new ResourceNotFoundException("Workout not found"));

        // Set workout = null to avoid null references
        List<ExerciseLog> exerciseLogs = exerciseLogRepository.findByWorkout(workout);
        for(ExerciseLog exerciseLog : exerciseLogs) {
            exerciseLog.setWorkout(null);
        }
        workoutRepository.delete(workout);
    }
    @Transactional
    public void deleteCompletedWorkout(Long workoutId) {
        CompletedWorkout completedWorkout = completedWorkoutRepository.findById(workoutId).orElseThrow(() -> new ResourceNotFoundException("Completed Workout not found"));
        completedWorkoutRepository.delete(completedWorkout);
    }
}
