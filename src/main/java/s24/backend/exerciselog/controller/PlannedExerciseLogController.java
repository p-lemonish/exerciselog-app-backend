package s24.backend.exerciselog.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.dto.PlannedExerciseLogDto;
import s24.backend.exerciselog.mapper.*;
import s24.backend.exerciselog.repository.*;
import s24.backend.exerciselog.util.SecurityUtils;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
public class PlannedExerciseLogController {
    @Autowired
    private PlannedExerciseLogRepository plannedExerciseLogRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlannedExerciseLogMapper plannedExerciseLogMapper;

    @Autowired
    private ExerciseMapper exerciseMapper;

    @GetMapping("/planned")
    public String getAllPlannedExerciseLogs(Model model) { // TODO take it into PlannedExerciseLogService
        User user = SecurityUtils.getCurrentUser();
        List<Exercise> exercises = exerciseRepository.findByUser(user);
        List<PlannedExerciseLog> plannedExerciseLogs = plannedExerciseLogRepository.findByUser(user);
        model.addAttribute("plannedExerciseLogDtos", plannedExerciseLogMapper.toDtoList(plannedExerciseLogs) );
        model.addAttribute("exerciseDtos", exerciseMapper.toDtoList(exercises));
        PlannedExerciseLogDto plannedExerciseLogDto = new PlannedExerciseLogDto();
        plannedExerciseLogDto.setUserId(user.getId());
        model.addAttribute("plannedExerciseLogDto", plannedExerciseLogDto);
        return "planned";
    }

    @PostMapping("/add-planned")
    public String addPlannedExerciseLog(@Valid @ModelAttribute PlannedExerciseLogDto plannedExerciseLogDto, BindingResult result, Model model) {
        if(result.hasErrors()) {
            User user = SecurityUtils.getCurrentUser();
            List<Exercise> exercises = exerciseRepository.findByUser(user);
            List<PlannedExerciseLog> plannedExerciseLogs = plannedExerciseLogRepository.findByUser(user);
            model.addAttribute("plannedExerciseLogDto", plannedExerciseLogDto);
            model.addAttribute("exerciseDtos", exerciseMapper.toDtoList(exercises));
            model.addAttribute("plannedExerciseLogDtos", plannedExerciseLogMapper.toDtoList(plannedExerciseLogs));
            return "planned";
        }

        Optional<Exercise> exerciseOptional = exerciseRepository.findByName(plannedExerciseLogDto.getExerciseName());
        Exercise exercise;
        if(exerciseOptional.isPresent()) {
            exercise = exerciseOptional.get();
        } else {
            if(plannedExerciseLogDto.getMuscleGroup().isEmpty()) {
                result.rejectValue("muscleGroup", "error.plannedExerciseLogDto", "Muscle group must be added if adding a new exercise name");
                User user = SecurityUtils.getCurrentUser();
                List<Exercise> exercises = exerciseRepository.findByUser(user);
                List<PlannedExerciseLog> plannedExerciseLogs = plannedExerciseLogRepository.findByUser(user);
                model.addAttribute("plannedExerciseLogDto", plannedExerciseLogDto);
                model.addAttribute("exerciseDtos", exerciseMapper.toDtoList(exercises));
                model.addAttribute("plannedExerciseLogDtos", plannedExerciseLogMapper.toDtoList(plannedExerciseLogs));
                return "planned";
            }
            exercise = new Exercise(plannedExerciseLogDto.getExerciseName(), plannedExerciseLogDto.getMuscleGroup());
            exerciseRepository.save(exercise);
        }

        User user = userRepository.findById(plannedExerciseLogDto.getUserId()).orElseThrow(() -> new RuntimeException("User with ID " + plannedExerciseLogDto.getUserId() + " not found"));
        PlannedExerciseLog plannedExerciseLog = plannedExerciseLogMapper.toEntity(plannedExerciseLogDto, user, exercise);
        plannedExerciseLog.setExercise(exercise);
        user.getPlannedExerciseLogs().add(plannedExerciseLog);
        userRepository.save(user);
        return "redirect:/planned";
    }

    @PostMapping("/delete-planned/{id}") //TODO let user delete planned workout with ExerciseLog persisting
    public String deletePlannedExerciseLog(@PathVariable Long id) {
        plannedExerciseLogRepository.deleteById(id);
        return "redirect:/planned";
    }
    
    @GetMapping("/edit-planned/{id}")
    public String showEditPlannedExerciseLogForm(@PathVariable Long id, Model model) {
        Optional<PlannedExerciseLog> plannedExerciseLogOptional = plannedExerciseLogRepository.findById(id);
        if (plannedExerciseLogOptional.isPresent()) {
            PlannedExerciseLogDto plannedExerciseLogDto = plannedExerciseLogMapper.toDto(plannedExerciseLogOptional.get());
            model.addAttribute("plannedExerciseLogDto", plannedExerciseLogDto);
            return "edit-planned";
        } else {
            return "redirect:/planned";
        }
    }

    @PostMapping("/edit-planned/{id}")
    public String updatePlannedExerciseLog(@Valid @ModelAttribute PlannedExerciseLogDto plannedExerciseLogDto, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("plannedExerciseLogDto", plannedExerciseLogDto);
            return "edit-planned";
        }

        Optional<Exercise> exerciseOptional = exerciseRepository.findByName(plannedExerciseLogDto.getExerciseName());
        Exercise exercise;
        if(exerciseOptional.isPresent()) {
            exercise = exerciseOptional.get();
        } else {
            if(plannedExerciseLogDto.getMuscleGroup().isEmpty()) {
                result.rejectValue("muscleGroup", "error.plannedExerciseLogDto", "Muscle group must be added if adding a new exercise name");
                model.addAttribute("plannedExerciseLogDto", plannedExerciseLogDto);
                return "edit-planned";
            }
            exercise = new Exercise(plannedExerciseLogDto.getExerciseName(), plannedExerciseLogDto.getMuscleGroup());
            exerciseRepository.save(exercise);
        }

        User user = userRepository.findById(plannedExerciseLogDto.getUserId()).orElseThrow(() -> new RuntimeException("User with ID " + plannedExerciseLogDto.getUserId() + " not found"));
        PlannedExerciseLog plannedExerciseLog = plannedExerciseLogMapper.toEntity(plannedExerciseLogDto, user, exercise);
        plannedExerciseLog.setExercise(exercise);
        user.getPlannedExerciseLogs().add(plannedExerciseLog);
        userRepository.save(user);
        return "redirect:/planned";
    }

}
