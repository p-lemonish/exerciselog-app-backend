package s24.backend.exerciselog.controller.rest;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.dto.*;
import s24.backend.exerciselog.service.WorkoutService;
import s24.backend.exerciselog.util.SecurityUtils;
import s24.backend.exerciselog.util.ValidationUtil;



@RestController
public class WorkoutRestController {
    @Autowired
    private WorkoutService workoutService;
    
    @GetMapping("/api/workouts")
    public ResponseEntity<List<WorkoutDto>> getAllWorkouts() {
        User user = SecurityUtils.getCurrentUser();
        List<WorkoutDto> workouts = workoutService.getUserWorkouts(user);
        return ResponseEntity.status(HttpStatus.OK).body(workouts);
    }

    @GetMapping("/api/workouts/completed")
    public ResponseEntity<List<CompletedWorkoutDto>> getAllCompletedWorkouts() {
        User user = SecurityUtils.getCurrentUser();
        List<CompletedWorkoutDto> completedWorkouts = workoutService.getUserCompletedWorkouts(user);
        return ResponseEntity.status(HttpStatus.OK).body(completedWorkouts);
    }


    @PostMapping("/api/workouts")
    public ResponseEntity<?> addWorkout(@Valid @RequestBody WorkoutDto workoutDto, BindingResult result) {
        ResponseEntity<Map<String, String>> validationErrors = ValidationUtil.handleValidationErrors(result);
        if(validationErrors != null) {
            return validationErrors;
        }

        User user = SecurityUtils.getCurrentUser();
        workoutService.addWorkout(workoutDto, user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
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
