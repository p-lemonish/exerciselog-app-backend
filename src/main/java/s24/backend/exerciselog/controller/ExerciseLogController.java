package s24.backend.exerciselog.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.repository.*;


@Controller
public class ExerciseLogController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExerciseLogRepository exerciseLogRepository;
    
    @GetMapping("/logs")
    public String showExerciseLogs(
        @RequestParam(value = "sort", required = false) String sort,
        @RequestParam(value = "exerciseName", required = false) String exerciseName,
        Model model) {
        User user = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("User not found")); //TODO placeholder findbyid

        //TODO filter by name (done), sort by date
        List<ExerciseLog> exerciseLogs;
        if(exerciseName != null && !exerciseName.isEmpty()) {
            exerciseLogs = exerciseLogRepository.findByUserAndName(user, exerciseName);
        } else if("name".equals(sort)) {
            exerciseLogs = exerciseLogRepository.findByUserOrderByNameAsc(user);
        } else {
            exerciseLogs = exerciseLogRepository.findByUser(user);
        }
        model.addAttribute("exerciseLogs", exerciseLogs);
        return "logs";
    }
    
}
