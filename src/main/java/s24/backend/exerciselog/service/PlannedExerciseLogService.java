package s24.backend.exerciselog.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import jakarta.transaction.Transactional;

import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.dto.*;
import s24.backend.exerciselog.mapper.*;
import s24.backend.exerciselog.repository.*;

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
    public void addPlannedExerciseLog(PlannedExerciseLogDto plannedExerciseLogDto, BindingResult result) {
        Exercise exercise = findOrCreateExercise(plannedExerciseLogDto, result);
        if(result.hasErrors()) {
            return;
        }
        User user = userRepository.findById(plannedExerciseLogDto.getUserId()).orElseThrow(() -> new RuntimeException("User with ID " + plannedExerciseLogDto.getUserId() + " not found"));
        PlannedExerciseLog plannedExerciseLog = plannedExerciseLogMapper.toEntity(plannedExerciseLogDto, user, exercise);
        plannedExerciseLogRepository.save(plannedExerciseLog);
    }

    @Transactional
    public PlannedExerciseLogDto getPlannedExerciseLogDtoById(Long id) {
        PlannedExerciseLog plannedExerciseLog = plannedExerciseLogRepository.findById(id).orElseThrow(() -> new RuntimeException("PlannedExerciseLog not found"));
        return plannedExerciseLogMapper.toDto(plannedExerciseLog);
    }

    @Transactional
    public void updatePlannedExerciseLog(PlannedExerciseLogDto plannedExerciseLogDto, BindingResult result) {
        Exercise exercise = findOrCreateExercise(plannedExerciseLogDto, result);
        if(result.hasErrors()) {
            return;
        }
        User user = userRepository.findById(plannedExerciseLogDto.getUserId()).orElseThrow(() -> new RuntimeException("User with ID " + plannedExerciseLogDto.getUserId() + " not found"));
        PlannedExerciseLog plannedExerciseLog = plannedExerciseLogMapper.toEntity(plannedExerciseLogDto, user, exercise);
        plannedExerciseLog.setExercise(exercise);
        user.getPlannedExerciseLogs().add(plannedExerciseLog);
        userRepository.save(user);
    }

    @Transactional
    public void deletePlannedExerciseLog(Long id) { //TODO handle deletion without errors
        PlannedExerciseLog plannedExerciseLog = plannedExerciseLogRepository.findById(id).orElseThrow(() -> new RuntimeException("Planned exercise log not found"));

        // Set completedWorkout = null to avoid null references
        List<ExerciseLog> exerciseLogs = exerciseLogRepository.findByPlannedExerciseLog(plannedExerciseLog);
        for(ExerciseLog exerciseLog : exerciseLogs) {
            exerciseLog.setPlannedExerciseLog(null);
        }
        plannedExerciseLogRepository.deleteById(id);
    }

    // Helper method for finding an existing exercise or creating a new one if not found
    private Exercise findOrCreateExercise(PlannedExerciseLogDto plannedExerciseLogDto, BindingResult result) {
        Optional<Exercise> exerciseOptional = exerciseRepository.findByName(plannedExerciseLogDto.getExerciseName());
        Exercise exercise;
        if(exerciseOptional.isPresent()) {
            exercise = exerciseOptional.get();
        } else {
            if(plannedExerciseLogDto.getMuscleGroup().isEmpty() || plannedExerciseLogDto.getMuscleGroup() == null) {
                result.rejectValue("muscleGroup", "error.plannedExerciseLogDto", "Muscle group must be added if adding a new exercise name");
                return null;
            }
            exercise = new Exercise(plannedExerciseLogDto.getExerciseName(), plannedExerciseLogDto.getMuscleGroup());
            exerciseRepository.save(exercise);
        }
        return exercise;
    }
}
