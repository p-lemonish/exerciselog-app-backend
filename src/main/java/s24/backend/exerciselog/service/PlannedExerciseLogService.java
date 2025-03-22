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
    public void addPlannedExerciseLog(PlannedExerciseLogDto plannedExerciseLogDto, Exercise exercise)
            throws BadRequestException {
        User user = SecurityUtils.getCurrentUser();
        PlannedExerciseLog plannedExerciseLog = plannedExerciseLogMapper.toEntity(plannedExerciseLogDto, user,
                exercise);
        plannedExerciseLogRepository.save(plannedExerciseLog);
    }

    @Transactional
    public PlannedExerciseLogDto getPlannedExerciseLogDtoById(Long id) throws BadRequestException {
        User user = SecurityUtils.getCurrentUser();
        PlannedExerciseLog plannedExerciseLog = plannedExerciseLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PlannedExerciseLog not found"));
        if (user.getId() != plannedExerciseLog.getUser().getId()) {
            throw new BadRequestException("You do not own this exercise!");
        }
        return plannedExerciseLogMapper.toDto(plannedExerciseLog);
    }

    @Transactional
    public void updatePlannedExerciseLog(PlannedExerciseLogDto plannedExerciseLogDto) throws BadRequestException {
        User user = SecurityUtils.getCurrentUser();
        PlannedExerciseLog plannedExerciseLog = plannedExerciseLogRepository.findById(plannedExerciseLogDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Planned Exercise not found."));
        if (user.getId() != plannedExerciseLog.getUser().getId()) {
            throw new BadRequestException("You do not own this exercise!");
        }

        plannedExerciseLog.setNotes(plannedExerciseLogDto.getNotes());
        plannedExerciseLog.getExercise().setName(plannedExerciseLogDto.getExerciseName());
        plannedExerciseLog.setPlannedReps(plannedExerciseLogDto.getPlannedReps());
        plannedExerciseLog.setPlannedSets(plannedExerciseLogDto.getPlannedSets());
        plannedExerciseLog.setPlannedWeight(plannedExerciseLogDto.getPlannedWeight());

        userRepository.save(user);
    }

    @Transactional
    public void deletePlannedExerciseLog(Long id) throws BadRequestException {
        User user = SecurityUtils.getCurrentUser();
        PlannedExerciseLog plannedExerciseLog = plannedExerciseLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PlannedExerciseLog not found"));
        if (user.getId() != plannedExerciseLog.getUser().getId()) {
            throw new BadRequestException("You do not own this exercise!");
        }

        List<Workout> workouts = workoutRepository.findByPlannedExerciseLogs(plannedExerciseLog);
        if (!(workouts.isEmpty() || workouts == null)) {
            throw new BadRequestException("Cannot delete a planned exercise that is being used by a planned workout!");
        }
        // Set completedWorkout = null to avoid null references
        List<ExerciseLog> exerciseLogs = exerciseLogRepository.findByPlannedExerciseLog(plannedExerciseLog);
        for (ExerciseLog exerciseLog : exerciseLogs) {
            exerciseLog.setPlannedExerciseLog(null);
        }
        plannedExerciseLogRepository.deleteById(id);
    }

    // Helper method for finding an existing exercise or creating a new one if not
    // found
    public Exercise findOrCreateExercise(PlannedExerciseLogDto plannedExerciseLogDto) throws BadRequestException {
        String exerciseName = plannedExerciseLogDto.getExerciseName();
        User user = SecurityUtils.getCurrentUser();
        List<Exercise> userExercises = exerciseRepository.findByUser(user);
        for (Exercise exercise : userExercises) {
            if (exercise.getName().equals(exerciseName)) {
                return exercise;
            }
        }
        Exercise exercise = new Exercise(plannedExerciseLogDto.getExerciseName());
        exercise.setUser(user);
        exerciseRepository.save(exercise);
        return exercise;
    }
}
