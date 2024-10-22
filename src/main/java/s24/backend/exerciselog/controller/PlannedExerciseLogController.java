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
    private PlannedExerciseLogMapper plannedExerciseLogFormMapper;

    @Autowired
    private ExerciseMapper exerciseMapper;

    @GetMapping("/planned")
    public String getAllPlannedExerciseLogs(Model model) { // TODO take it into PlannedExerciseLogService
        User user = SecurityUtils.getCurrentUser();
        List<Exercise> exercises = exerciseRepository.findByUser(user);
        List<PlannedExerciseLog> plannedExerciseLogs = plannedExerciseLogRepository.findByUser(user);
        model.addAttribute("plannedExerciseLogDtos", plannedExerciseLogFormMapper.toDtoList(plannedExerciseLogs) );
        model.addAttribute("exerciseDtos", exerciseMapper.toDtoList(exercises));
        PlannedExerciseLogDto plannedExerciseLogForm = new PlannedExerciseLogDto();
        plannedExerciseLogForm.setUserId(user.getId());
        model.addAttribute("plannedExerciseLogForm", plannedExerciseLogForm);
        return "planned";
    }

    @PostMapping("/add-planned")
    public String addPlannedExerciseLog(@Valid @ModelAttribute PlannedExerciseLogDto plannedExerciseLogForm, BindingResult result, Model model) {
        if(result.hasErrors()) {
            User user = SecurityUtils.getCurrentUser();
            List<Exercise> exercises = exerciseRepository.findByUser(user);
            List<PlannedExerciseLog> plannedExerciseLogs = plannedExerciseLogRepository.findByUser(user);
            model.addAttribute("plannedExerciseLogForm", plannedExerciseLogForm);
            model.addAttribute("exerciseDtos", exerciseMapper.toDtoList(exercises));
            model.addAttribute("plannedExerciseLogDtos", plannedExerciseLogFormMapper.toDtoList(plannedExerciseLogs));
            return "planned";
        }

        Optional<Exercise> exerciseOptional = exerciseRepository.findByName(plannedExerciseLogForm.getExerciseName());
        Exercise exercise;
        if(exerciseOptional.isPresent()) {
            exercise = exerciseOptional.get();
        } else {
            if(plannedExerciseLogForm.getMuscleGroup().isEmpty()) {
                result.rejectValue("muscleGroup", "error.plannedExerciseLogForm", "Muscle group must be added if adding a new exercise name");
                User user = SecurityUtils.getCurrentUser();
                List<Exercise> exercises = exerciseRepository.findByUser(user);
                List<PlannedExerciseLog> plannedExerciseLogs = plannedExerciseLogRepository.findByUser(user);
                model.addAttribute("plannedExerciseLogForm", plannedExerciseLogForm);
                model.addAttribute("exerciseDtos", exerciseMapper.toDtoList(exercises));
                model.addAttribute("plannedExerciseLogDtos", plannedExerciseLogFormMapper.toDtoList(plannedExerciseLogs));
                return "planned";
            }
            exercise = new Exercise(plannedExerciseLogForm.getExerciseName(), plannedExerciseLogForm.getMuscleGroup());
            exerciseRepository.save(exercise);
        }

        User user = userRepository.findById(plannedExerciseLogForm.getUserId()).orElseThrow(() -> new RuntimeException("User with ID " + plannedExerciseLogForm.getUserId() + " not found"));
        PlannedExerciseLog plannedExerciseLog = plannedExerciseLogFormMapper.toEntity(plannedExerciseLogForm, user, exercise);
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
            PlannedExerciseLogDto plannedExerciseLogForm = plannedExerciseLogFormMapper.toDto(plannedExerciseLogOptional.get());
            model.addAttribute("plannedExerciseLogForm", plannedExerciseLogForm);
            return "edit-planned";
        } else {
            return "redirect:/planned";
        }
    }

    @PostMapping("/edit-planned/{id}")
    public String updatePlannedExerciseLog(@Valid @ModelAttribute PlannedExerciseLogDto plannedExerciseLogForm, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("plannedExerciseLogForm", plannedExerciseLogForm);
            return "edit-planned";
        }

        Optional<Exercise> exerciseOptional = exerciseRepository.findByName(plannedExerciseLogForm.getExerciseName());
        Exercise exercise;
        if(exerciseOptional.isPresent()) {
            exercise = exerciseOptional.get();
        } else {
            if(plannedExerciseLogForm.getMuscleGroup().isEmpty()) {
                result.rejectValue("muscleGroup", "error.plannedExerciseLogForm", "Muscle group must be added if adding a new exercise name");
                model.addAttribute("plannedExerciseLogForm", plannedExerciseLogForm);
                return "edit-planned";
            }
            exercise = new Exercise(plannedExerciseLogForm.getExerciseName(), plannedExerciseLogForm.getMuscleGroup());
            exerciseRepository.save(exercise);
        }

        User user = userRepository.findById(plannedExerciseLogForm.getUserId()).orElseThrow(() -> new RuntimeException("User with ID " + plannedExerciseLogForm.getUserId() + " not found"));
        PlannedExerciseLog plannedExerciseLog = plannedExerciseLogFormMapper.toEntity(plannedExerciseLogForm, user, exercise);
        plannedExerciseLog.setExercise(exercise);
        user.getPlannedExerciseLogs().add(plannedExerciseLog);
        userRepository.save(user);
        return "redirect:/planned";
    }

}
