package s24.backend.exerciselog.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.dto.ExerciseLogDto;
import s24.backend.exerciselog.mapper.ExerciseLogMapper;
import s24.backend.exerciselog.repository.*;
import s24.backend.exerciselog.util.SecurityUtils;


@Controller
public class ExerciseLogController {
    @Autowired
    private ExerciseLogRepository exerciseLogRepository;
    @Autowired
    private ExerciseLogMapper exerciseLogMapper;
    
    @GetMapping("/logs")
    public String showExerciseLogs(
        @RequestParam(value = "sort", required = false) String sort,
        @RequestParam(value = "exerciseName", required = false) String exerciseName,
        Model model) {
        User user = SecurityUtils.getCurrentUser();
        List<ExerciseLog> exerciseLogs;
        if(exerciseName != null && !exerciseName.isEmpty()) {
            exerciseLogs = exerciseLogRepository.findByUserAndNameIgnoreCaseOrderByCompletedWorkout_DateDesc(user, exerciseName);
        } else {
            exerciseLogs = exerciseLogRepository.findByUserOrderByCompletedWorkout_DateDesc(user);
        }
        List<ExerciseLogDto> exerciseLogDtoList = exerciseLogMapper.toDtoList(exerciseLogs);
        model.addAttribute("exerciseLogs", exerciseLogDtoList);
        return "logs";
    }
    
}
