package s24.backend.exerciselog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import s24.backend.exerciselog.repository.ExerciseRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ExerciseController {
    @Autowired
    private ExerciseRepository exerciseRepository;

    @GetMapping("path")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
}
