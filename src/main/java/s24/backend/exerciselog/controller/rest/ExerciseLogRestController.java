package s24.backend.exerciselog.controller.rest;

import org.springframework.web.bind.annotation.*;

@RestController
public class ExerciseLogRestController {
    
    @GetMapping("/api/logs")
    public String showExerciseLogs(@RequestParam String param) {
        //TODO
        return new String();
    }
    
}
