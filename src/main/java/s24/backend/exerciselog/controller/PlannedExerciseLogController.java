package s24.backend.exerciselog.controller;

import java.util.*;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.dto.*;
import s24.backend.exerciselog.service.PlannedExerciseLogService;
import s24.backend.exerciselog.util.SecurityUtils;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
public class PlannedExerciseLogController {
    @Autowired
    private PlannedExerciseLogService plannedExerciseLogService;

    @GetMapping("/planned")
    public String getAllPlannedExerciseLogs(Model model) {
        User user = SecurityUtils.getCurrentUser();

        // Show all planned exercises by user
        List<ExerciseDto> exerciseDtos = plannedExerciseLogService.getUserExercises(user);
        List<PlannedExerciseLogDto> plannedExerciseLogDtos = plannedExerciseLogService.getAllPlannedExerciseLogs(user);
        model.addAttribute("plannedExerciseLogDtos", plannedExerciseLogDtos);
        model.addAttribute("exerciseDtos", exerciseDtos);

        // Creation of new exercise
        PlannedExerciseLogDto plannedExerciseLogDto = new PlannedExerciseLogDto();
        plannedExerciseLogDto.setUserId(user.getId());
        model.addAttribute("plannedExerciseLogDto", plannedExerciseLogDto);
        return "planned";
    }

    @PostMapping("/add-planned")
    public String addPlannedExerciseLog(@Valid @ModelAttribute PlannedExerciseLogDto plannedExerciseLogDto, BindingResult result, Model model) {

        // Check for general validation errors
        if(result.hasErrors()) {
            // Reload user data
            User user = SecurityUtils.getCurrentUser();
            List<ExerciseDto> exerciseDtos = plannedExerciseLogService.getUserExercises(user);
            List<PlannedExerciseLogDto> plannedExerciseLogDtos = plannedExerciseLogService.getAllPlannedExerciseLogs(user);
            model.addAttribute("plannedExerciseLogDtos", plannedExerciseLogDtos);
            model.addAttribute("exerciseDtos", exerciseDtos);
            return "planned";
        }
        
        plannedExerciseLogService.addPlannedExerciseLog(plannedExerciseLogDto, result);

        // Check once more if errors arised with muscleGroup missing
        if(result.hasErrors()) {
            // Reload user data
            User user = SecurityUtils.getCurrentUser();
            List<ExerciseDto> exerciseDtos = plannedExerciseLogService.getUserExercises(user);
            List<PlannedExerciseLogDto> plannedExerciseLogDtos = plannedExerciseLogService.getAllPlannedExerciseLogs(user);
            model.addAttribute("plannedExerciseLogDtos", plannedExerciseLogDtos);
            model.addAttribute("exerciseDtos", exerciseDtos);
            return "planned";
        }
        return "redirect:/planned";
    }

    @PostMapping("/delete-planned/{id}")
    public String deletePlannedExerciseLog(@PathVariable Long id) throws BadRequestException {
        plannedExerciseLogService.deletePlannedExerciseLog(id);
        return "redirect:/planned";
    }
    
    @GetMapping("/edit-planned/{id}")
    public String showEditPlannedExerciseLogForm(@PathVariable Long id, Model model) {
        PlannedExerciseLogDto plannedExerciseLogDto = plannedExerciseLogService.getPlannedExerciseLogDtoById(id);
        model.addAttribute("plannedExerciseLogDto", plannedExerciseLogDto);
        return "edit-planned";
    }

    @PostMapping("/edit-planned/{id}")
    public String updatePlannedExerciseLog(@Valid @ModelAttribute PlannedExerciseLogDto plannedExerciseLogDto, BindingResult result, Model model) {

        // Check for general validation errors
        if(result.hasErrors()) {
            return "edit-planned";
        }

        plannedExerciseLogService.updatePlannedExerciseLog(plannedExerciseLogDto, result);

        // Check once more for errors with muscleGroup
        if(result.hasErrors()) {
            return "edit-planned";
        }
        return "redirect:/planned";
    }

}
