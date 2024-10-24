package s24.backend.exerciselog.controller.rest;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class WorkoutRestController {
    
    @GetMapping("/api/workouts")
    public String getAllWorkouts(@RequestParam String param) {
        //TODO
        return new String();
    }

    @PostMapping("/api/workouts")
    public String addWorkout(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    @GetMapping("/api/workouts/start/{id}")
    public String startWorkout(@RequestParam String param) {
        //TODO
        return new String();
    }

    @PostMapping("/api/workouts/complete/{id}")
    public String completeWorkout(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    @DeleteMapping("/api/workouts/delete-planned/{id}")
    public String deletePlannedWorkout(@PathVariable Long id) {
        //TODO
        return null;
    }
    
    @DeleteMapping("/api/workouts/delete-completed/{id}")
    public String deleteCompletedWorkout(@PathVariable Long id) {
        //TODO
        return null;
    }
}
