package s24.backend.exerciselog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ViewController {
    @GetMapping("/home")
    public String getHome() {
        return "home";
    }
    @GetMapping("/logs")
    public String getLogs() {
        return "logs";
    }
    @GetMapping("/planned")
    public String getPlanned() {
        return "planned";
    }
    @GetMapping("/profile")
    public String getProfile() {
        return "profile";
    }
    @GetMapping("/workouts")
    public String getWorkouts() {
        return "workouts";
    }
    
}
