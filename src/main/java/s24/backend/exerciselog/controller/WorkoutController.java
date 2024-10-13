package s24.backend.exerciselog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import s24.backend.exerciselog.repository.PlannedExerciseLogRepository;
import s24.backend.exerciselog.repository.UserRepository;
import s24.backend.exerciselog.repository.WorkoutRepository;
import s24.backend.exerciselog.domain.CompletedWorkout;
import s24.backend.exerciselog.domain.ExerciseLog;
import s24.backend.exerciselog.domain.PlannedExerciseLog;
import s24.backend.exerciselog.domain.User;
import s24.backend.exerciselog.domain.Workout;
import s24.backend.exerciselog.repository.CompletedWorkoutRepository;
import s24.backend.exerciselog.repository.ExerciseLogRepository;

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

    @Autowired
    private ExerciseLogRepository exerciseLogRepository;

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
        User user = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("User not found")); //TODO placeholder findbyid

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
        User user = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("User not found")); //TODO placeholder findbyid

        Optional<Workout> workoutOptional = workoutRepository.findById(id);
        if(!workoutOptional.isPresent()) {
            throw new RuntimeException("Workout not found");
        }
        Workout workout = workoutOptional.get();

        CompletedWorkout completedWorkout = new CompletedWorkout();
        LocalDate date = LocalDate.now();
        completedWorkout.setUser(user);
        completedWorkout.setDate(date);
        completedWorkout.setNotes(notes);
        completedWorkout.setWorkoutName(workout.getName());
        completedWorkout.setWorkoutNotes(workout.getNotes());
        completedWorkout.setPlannedDate(workout.getDate());

        completedWorkoutRepository.save(completedWorkout);

        for(PlannedExerciseLog plannedExerciseLog : workout.getPlannedExerciseLogs()) {
            ExerciseLog exerciseLog = new ExerciseLog();
            exerciseLog.setUser(user);
            exerciseLog.setCompletedWorkout(completedWorkout);
            exerciseLog.setExercise(plannedExerciseLog.getExercise());
            exerciseLog.setWorkout(workout);
            exerciseLog.setName(plannedExerciseLog.getExercise().getName());
            exerciseLog.setPlannedExerciseLog(plannedExerciseLog);
            exerciseLog.setNotes(notes);
            // TODO SetLog
            exerciseLogRepository.save(exerciseLog);
        }
        //user.getWorkouts().remove(workout);

        return "redirect:/workouts";
    }

    @PostMapping("/workouts/delete-planned-workout/{id}")
    public String deletePlannedWorkout(@PathVariable Long id) {
        Optional<Workout> workoutOptional = workoutRepository.findById(id);
        if (workoutOptional.isPresent()) {
            Workout workout = workoutOptional.get();
            workoutRepository.delete(workout);
        } else {
            throw new RuntimeException("Planned workout not found.");
        }
        return "redirect:/workouts";
    }

    @PostMapping("/workouts/delete-completed-workout/{id}")
    public String deleteCompletedWorkout(@PathVariable Long id) {
        Optional<CompletedWorkout> completedWorkoutOptional = completedWorkoutRepository.findById(id);
        if (completedWorkoutOptional.isPresent()) {
            CompletedWorkout completedWorkout = completedWorkoutOptional.get();
            completedWorkoutRepository.delete(completedWorkout);
        } else {
            throw new RuntimeException("Completed workout not found.");
        }
        return "redirect:/workouts";
    }
}
