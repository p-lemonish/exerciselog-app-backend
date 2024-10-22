package s24.backend.exerciselog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.dto.*;
import s24.backend.exerciselog.repository.WorkoutRepository;
import s24.backend.exerciselog.service.WorkoutService;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.*;
import java.time.LocalDate;

@Controller
public class WorkoutController { // TODO user input validation
    @Autowired
    private WorkoutService workoutService;
    @Autowired
    private WorkoutRepository workoutRepository;

    @GetMapping("/workouts")
    public String getWorkoutsPage(Model model) {
        workoutService.getAllAttributes(model);
        return "workouts";
    }
    
    @PostMapping("/add-workout")
    public String addWorkout(@RequestParam String name, 
        @RequestParam LocalDate date, 
        @RequestParam(required = false) String notes, 
        @RequestParam (required = false, defaultValue = "") List<Long> plannedExerciseIds,
        Model model) {
        if(plannedExerciseIds == null || plannedExerciseIds.isEmpty()) {
            model.addAttribute("error", "Please select at least one exercise");
            workoutService.getAllAttributes(model);
            return "workouts";
        } else {
            workoutService.addWorkout(name, date, notes, plannedExerciseIds);
            return "redirect:/workouts";
        }
    }

    @GetMapping("/workouts/start/{id}")
    public String startWorkout(@PathVariable Long id, Model model) {
        workoutService.startWorkout(id, model);
        return "start-workout";
    }

    @PostMapping("/workouts/complete/{id}")
    public String completeWorkout(@PathVariable Long id, 
        @Valid @ModelAttribute CompletedWorkoutDto completedWorkoutDto, BindingResult result, Model model) {
        if(result.hasErrors()) {
            //completedWorkoutDto will be missing workoutName, workoutDate, workoutId 
            Workout workout = workoutRepository.findById(id).orElseThrow(() -> new RuntimeException("Workout not found"));
            completedWorkoutDto.setWorkoutName(workout.getName());
            completedWorkoutDto.setPlannedDate(workout.getDate());
            completedWorkoutDto.setId(id);
            model.addAttribute("completedWorkoutDto", completedWorkoutDto);
            return "start-workout";
        }
        workoutService.completeWorkout(id, completedWorkoutDto);
        return "redirect:/workouts";
    }

    @PostMapping("/workouts/delete-planned-workout/{id}")
    public String deletePlannedWorkout(@PathVariable Long id) {
        workoutService.deletePlannedWorkout(id);
        return "redirect:/workouts";
    }

    /* Perhaps dont let it happen TODO decide if let user delete completed workouts, if yes => delete logs or no?
    @PostMapping("/workouts/delete-completed-workout/{id}")
    public String deleteCompletedWorkout(@PathVariable Long id) {
        workoutService.deleteCompletedWorkout(id);
        return "redirect:/workouts";
    }
     */
}
