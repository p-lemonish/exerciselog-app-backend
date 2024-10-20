package s24.backend.exerciselog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import s24.backend.exerciselog.dto.WorkoutCompletionForm;
import s24.backend.exerciselog.service.WorkoutService;

import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.time.LocalDate;

@Controller
public class WorkoutController { // TODO user input validation
    @Autowired
    private WorkoutService workoutService;

    @GetMapping("/workouts")
    public String getWorkoutsPage(Model model) {
        workoutService.getAllAttributes(model);
        return "workouts";
    }
    
    @PostMapping("/add-workout")
    public String addWorkout(@RequestParam String name, 
                            @RequestParam LocalDate date, 
                            @RequestParam(required = false) String notes, 
                            @RequestParam List<Long> plannedExerciseIds) {
        workoutService.addWorkout(name, date, notes, plannedExerciseIds);
        return "redirect:/workouts";
    }

    @GetMapping("/workouts/start/{id}")
    public String startWorkout(@PathVariable Long id, Model model) {
        workoutService.startWorkout(id, model);
        return "start-workout";
    }

    @PostMapping("/workouts/complete/{id}")
    public String completeWorkout(@PathVariable Long id, @ModelAttribute WorkoutCompletionForm workoutCompletionForm) {
        workoutService.completeWorkout(id, workoutCompletionForm);
        return "redirect:/workouts";
    }

    @PostMapping("/workouts/delete-planned-workout/{id}")
    public String deletePlannedWorkout(@PathVariable Long id) {
        workoutService.deletePlannedWorkout(id);
        return "redirect:/workouts";
    }

    @PostMapping("/workouts/delete-completed-workout/{id}")
    public String deleteCompletedWorkout(@PathVariable Long id) {
        workoutService.deleteCompletedWorkout(id);
        return "redirect:/workouts";
    }
}
