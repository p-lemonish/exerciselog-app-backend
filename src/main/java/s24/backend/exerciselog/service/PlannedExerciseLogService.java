package s24.backend.exerciselog.service;

import java.util.*;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import s24.backend.exerciselog.domain.dto.*;
import s24.backend.exerciselog.domain.entity.Exercise;
import s24.backend.exerciselog.domain.entity.ExerciseLog;
import s24.backend.exerciselog.domain.entity.PlannedExerciseLog;
import s24.backend.exerciselog.domain.entity.User;
import s24.backend.exerciselog.domain.entity.Workout;
import s24.backend.exerciselog.exception.ResourceNotFoundException;
import s24.backend.exerciselog.mapper.*;
import s24.backend.exerciselog.repository.*;
import s24.backend.exerciselog.util.SecurityUtils;

@Service
public class PlannedExerciseLogService {
    @Autowired
    private PlannedExerciseLogRepository plannedExerciseLogRepository;
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private PlannedExerciseLogMapper plannedExerciseLogMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExerciseMapper exerciseMapper;
    @Autowired
    private ExerciseLogRepository exerciseLogRepository;
    @Autowired
    private WorkoutRepository workoutRepository;

    @Transactional
    public List<PlannedExerciseLogDto> getAllPlannedExerciseLogs(User user) {
        List<PlannedExerciseLog> plannedExerciseLogs = plannedExerciseLogRepository.findByUser(user);
        return plannedExerciseLogMapper.toDtoList(plannedExerciseLogs);
    }

    @Transactional
    public List<ExerciseDto> getUserExercises(User user) {
        List<Exercise> exercises = exerciseRepository.findByUser(user);
        return exerciseMapper.toDtoList(exercises);
    }

    @Transactional
    public void addPlannedExerciseLog(PlannedExerciseLogDto plannedExerciseLogDto, Exercise exercise) throws BadRequestException {
        User user = SecurityUtils.getCurrentUser();
        if(plannedExerciseLogDto.getId() != null || plannedExerciseLogDto.getUserId() != null) {
            throw new BadRequestException("UserId or PlannedExerciseLogId must not be present while adding new planned exercise");
        }
        PlannedExerciseLog plannedExerciseLog = plannedExerciseLogMapper.toEntity(plannedExerciseLogDto, user, exercise);
        plannedExerciseLogRepository.save(plannedExerciseLog);
    }

    @Transactional
    public PlannedExerciseLogDto getPlannedExerciseLogDtoById(Long id) {
        PlannedExerciseLog plannedExerciseLog = plannedExerciseLogRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PlannedExerciseLog not found"));
        return plannedExerciseLogMapper.toDto(plannedExerciseLog);
    }

    @Transactional
    public void updatePlannedExerciseLog(PlannedExerciseLogDto plannedExerciseLogDto) throws BadRequestException {
        User user = SecurityUtils.getCurrentUser();
        if(plannedExerciseLogDto.getUserId() != null) {
            throw new BadRequestException("UserId must not be present while adding new planned exercise");
        }

        Optional<Exercise> exerciseOptional = exerciseRepository.findByName(plannedExerciseLogDto.getExerciseName());
        Exercise exercise;
        if(!exerciseOptional.isPresent()) {
            if((plannedExerciseLogDto.getMuscleGroup().isBlank()) || (plannedExerciseLogDto.getMuscleGroup() == null)) {
                throw new BadRequestException("Can't set muscle group as null or empty");
            }
            exercise = new Exercise(plannedExerciseLogDto.getExerciseName(), plannedExerciseLogDto.getMuscleGroup());
            exerciseRepository.save(exercise);
        } else {
            exercise = exerciseOptional.get();
        }

        // Check if user changed exercise name or muscle group
        if(!(plannedExerciseLogDto.getExerciseName().equals(exercise.getName())
            && plannedExerciseLogDto.getMuscleGroup().equals(exercise.getMuscleGroup()))) {
            
            if((plannedExerciseLogDto.getMuscleGroup().isBlank()) || (plannedExerciseLogDto.getMuscleGroup() == null)) {
                throw new BadRequestException("Can't set muscle group as null or empty");
            }
            exercise.setName(plannedExerciseLogDto.getExerciseName());
            exercise.setMuscleGroup(plannedExerciseLogDto.getMuscleGroup());
        }

        PlannedExerciseLog plannedExerciseLog = plannedExerciseLogMapper.toEntity(plannedExerciseLogDto, user, exercise);
        plannedExerciseLog.setExercise(exercise);
        user.getPlannedExerciseLogs().add(plannedExerciseLog);
        userRepository.save(user);
    }

    @Transactional
    public void deletePlannedExerciseLog(Long id) throws BadRequestException { 
        PlannedExerciseLog plannedExerciseLog = plannedExerciseLogRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Planned exercise log not found"));
        List<Workout> workouts = workoutRepository.findByPlannedExerciseLogs(plannedExerciseLog);
        if(!(workouts.isEmpty() || workouts == null)) {
            throw new BadRequestException("Cannot delete a planned exercise that is being used by a planned workout!");
        }
        // Set completedWorkout = null to avoid null references
        List<ExerciseLog> exerciseLogs = exerciseLogRepository.findByPlannedExerciseLog(plannedExerciseLog);
        for(ExerciseLog exerciseLog : exerciseLogs) {
            exerciseLog.setPlannedExerciseLog(null);
        }
        plannedExerciseLogRepository.deleteById(id);
    }

    // Helper method for finding an existing exercise or creating a new one if not found
    public Exercise findOrCreateExercise(PlannedExerciseLogDto plannedExerciseLogDto) throws BadRequestException {
        Optional<Exercise> exerciseOptional = exerciseRepository.findByName(plannedExerciseLogDto.getExerciseName());
        Exercise exercise;
        if(exerciseOptional.isPresent()) {
            exercise = exerciseOptional.get();
        } else {
            if(plannedExerciseLogDto.getMuscleGroup().isEmpty() || plannedExerciseLogDto.getMuscleGroup() == null) {
                throw new BadRequestException("Muscle group must be added if adding a new exercise name");
            }
            exercise = new Exercise(plannedExerciseLogDto.getExerciseName(), plannedExerciseLogDto.getMuscleGroup());
            exerciseRepository.save(exercise);
        }
        return exercise;
    }
}
