package s24.backend.exerciselog.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import s24.backend.exerciselog.domain.dto.*;
import s24.backend.exerciselog.domain.entity.CompletedWorkout;
import s24.backend.exerciselog.domain.entity.ExerciseLog;
import s24.backend.exerciselog.domain.entity.PlannedExerciseLog;
import s24.backend.exerciselog.domain.entity.User;
import s24.backend.exerciselog.domain.entity.Workout;
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
    public void addWorkout(WorkoutDto workoutDto, User user) {

        List<Long> selectedExerciseIds = workoutDto.getSelectedExerciseIds();
        List<PlannedExerciseLog> selectedPlannedExerciseLogs = plannedExerciseLogRepository.findAllById(selectedExerciseIds);
        Workout workout = workoutMapper.toEntity(workoutDto, user);
        workout.setPlannedExerciseLogs(selectedPlannedExerciseLogs);
        workoutRepository.save(workout);
    }

    @Transactional // TODO user can send API without exerciseName, causing it to be null
    public void completeWorkout(Long workoutId, CompletedWorkoutDto completedWorkoutDto) throws BadRequestException {
        User currentUser = SecurityUtils.getCurrentUser();
        Workout workout = workoutRepository.findById(workoutId)
            .orElseThrow(() -> new ResourceNotFoundException("Workout not found"));
        
        // Check if exerciseIds were changed in API request
        WorkoutDto workoutDto = workoutMapper.toDto(workout);
        List<Long> selectedExerciseIds = workoutDto.getSelectedExerciseIds();

        List<ExerciseLogDto> exerciseLogDtos = completedWorkoutDto.getExercises();
        List<Long> completedWorkoutDtoExerciseIds = exerciseLogDtos
            .stream().map(ExerciseLogDto::getExerciseId)
            .collect(Collectors.toList());
        
        if(!selectedExerciseIds.equals(completedWorkoutDtoExerciseIds)) {
            throw new BadRequestException("SelectedExerciseIds and completedWorkoutDtoExerciseIds do not match");
        }
        
        LocalDate now = LocalDate.now();
        CompletedWorkout completedWorkout = completedWorkoutMapper.toEntity(completedWorkoutDto);
        completedWorkout.setUser(currentUser);
        completedWorkout.setDate(now);
        completedWorkout.setWorkoutName(workout.getName());
        completedWorkout.setPlannedDate(workout.getDate());

        // Create an ExerciseLog instance of the completed workout
        List<ExerciseLog> exerciseLogs = new ArrayList<>();
        for (ExerciseLogDto exerciseDto : completedWorkoutDto.getExercises()) {
            PlannedExerciseLog plannedExerciseLog = plannedExerciseLogRepository.findById(exerciseDto.getExerciseId())
                .orElseThrow(() -> new ResourceNotFoundException("Planned Exercise not found"));

            // Check if setNumber in setLogDto was tampered with
            if(exerciseDto.getSetLogDtoList() != null) {
                int plannedSets = plannedExerciseLog.getPlannedSets();
                List<Integer> expectedSetNumbers = IntStream.rangeClosed(1, plannedSets).boxed().collect(Collectors.toList());
                List<Integer> givenSetNumbers = exerciseDto.getSetLogDtoList().stream().map(SetLogDto::getSetNumber).collect(Collectors.toList());
                if(!expectedSetNumbers.equals(givenSetNumbers)) {
                    throw new BadRequestException("Set number values are not as expected");
                }
            }
        
            ExerciseLog exerciseLog = exerciseLogMapper.toEntity(
                exerciseDto,
                new ExerciseLog(),
                plannedExerciseLog,
                currentUser,
                workout
            );
            exerciseLog.setDate(now);
            exerciseLogs.add(exerciseLog);
            exerciseLogRepository.save(exerciseLog);
        }
        completedWorkout.setExerciseLogs(exerciseLogs);
        completedWorkoutRepository.save(completedWorkout);
    }

    @Transactional
    public CompletedWorkoutDto startWorkout(Long workoutId) {
        Workout workout = workoutRepository.findById(workoutId).orElseThrow(() -> new ResourceNotFoundException("Workout not found"));

        CompletedWorkoutDto completedWorkoutDto = new CompletedWorkoutDto();
        completedWorkoutDto.setWorkoutName(workout.getName());
        completedWorkoutDto.setPlannedDate(workout.getDate());
        completedWorkoutDto.setId(workoutId);
        if(workout.getUser() != null) {
            completedWorkoutDto.setUserId(workout.getUser().getId());
        }

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
