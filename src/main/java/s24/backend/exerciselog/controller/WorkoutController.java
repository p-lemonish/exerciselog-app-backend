package s24.backend.exerciselog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import s24.backend.exerciselog.repository.PlannedExerciseLogRepository;
import s24.backend.exerciselog.repository.UserRepository;
import s24.backend.exerciselog.repository.WorkoutRepository;
import s24.backend.exerciselog.domain.CompletedWorkout;
import s24.backend.exerciselog.domain.PlannedExerciseLog;
import s24.backend.exerciselog.domain.User;
import s24.backend.exerciselog.domain.Workout;
import s24.backend.exerciselog.repository.CompletedWorkoutRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Controller
public class WorkoutController {
    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private PlannedExerciseLogRepository plannedExerciseLogRepository;

    @Autowired
    private CompletedWorkoutRepository completedWorkoutRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/workouts")
    public String getWorkoutsPage(Model model) {
        model.addAttribute("workouts", workoutRepository.findAll());
        model.addAttribute("completedWorkouts", completedWorkoutRepository.findAll());
        model.addAttribute("plannedExercises", plannedExerciseLogRepository.findAll());
        return "workouts";
    }
    
    @PostMapping("/add-workout")
    public String addWorkout(@RequestParam String name, 
                            @RequestParam LocalDate date, 
                            @RequestParam(required = false) String notes, 
                            @RequestParam List<Long> plannedExerciseIds) {
        Optional<User> userOptional = userRepository.findById(1L); //TODO placeholder
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();

        List<PlannedExerciseLog> selectedPlannedExercises = new ArrayList<>();
        if(plannedExerciseIds != null && !plannedExerciseIds.isEmpty()) {
            selectedPlannedExercises = plannedExerciseLogRepository.findAllById(plannedExerciseIds);
        }
        
        Workout newWorkout = new Workout(user, selectedPlannedExercises, name, notes, date);
        workoutRepository.save(newWorkout);

        return "redirect:/workouts";
    }

    @GetMapping("/workouts/start/{id}")
    public String startWorkout(@PathVariable Long id, Model model) {
        Optional<Workout> workoutOptional = workoutRepository.findById(id);
        Workout workout;
        if(workoutOptional.isPresent()) {
            workout = workoutOptional.get();
            model.addAttribute("workout", workout);
        } else {
            throw new RuntimeException("Workout not found");
        }
        return "start-workout";
    }

    @PostMapping("/workouts/complete/{id}")
    public String completeWorkout(@PathVariable Long id, @RequestParam(required = false) String notes) {
        Optional<User> userOptional = userRepository.findById(1L); //TODO placeholder
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();

        Optional<Workout> workoutOptional = workoutRepository.findById(id);
        if(!workoutOptional.isPresent()) {
            throw new RuntimeException("Workout not found");
        }
        Workout workout = workoutOptional.get();

        CompletedWorkout completedWorkout = new CompletedWorkout();
        LocalDate date = LocalDate.now();
        completedWorkout.setUser(user);
        completedWorkout.setDate(date);
        completedWorkout.setWorkout(workout);
        completedWorkout.setNotes(notes);

        completedWorkoutRepository.save(completedWorkout);
        user.getWorkouts().remove(workout);

        return "redirect:/workouts";
    }
}
