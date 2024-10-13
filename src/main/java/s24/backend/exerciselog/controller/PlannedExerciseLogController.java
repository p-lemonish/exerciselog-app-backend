package s24.backend.exerciselog.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import s24.backend.exerciselog.domain.Exercise;
import s24.backend.exerciselog.domain.PlannedExerciseLog;
import s24.backend.exerciselog.domain.User;
import s24.backend.exerciselog.repository.ExerciseRepository;
import s24.backend.exerciselog.repository.PlannedExerciseLogRepository;
import s24.backend.exerciselog.repository.UserRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PlannedExerciseLogController {
    @Autowired
    private PlannedExerciseLogRepository plannedExerciseLogRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/planned")
    public String getAllPlannedExerciseLogs(Model model) {
        model.addAttribute("plannedExerciseLogs", plannedExerciseLogRepository.findAll());
        model.addAttribute("exercises", exerciseRepository.findAll());
        return "planned";
    }

    @PostMapping("/add-planned")
    public String addPlannedExerciseLog(
            @RequestParam String name, 
            @RequestParam String muscleGroup, 
            @RequestParam int goalSets, 
            @RequestParam int goalReps, 
            @RequestParam double goalWeight, 
            @RequestParam String notes) {
        Optional<Exercise> exerciseOptional = exerciseRepository.findByName(name);
        Exercise exercise;
        if(exerciseOptional.isPresent()) {
            exercise = exerciseOptional.get();
        } else {
            exercise = new Exercise(name, muscleGroup);
            exerciseRepository.save(exercise);
        }

        User user = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("User not found")); //TODO placeholder findbyid

        PlannedExerciseLog plannedExerciseLog = new PlannedExerciseLog();
        plannedExerciseLog.setExercise(exercise);
        plannedExerciseLog.setPlannedReps(goalReps);
        plannedExerciseLog.setPlannedSets(goalSets);
        plannedExerciseLog.setPlannedWeight(goalWeight);
        plannedExerciseLog.setNotes(notes);
        plannedExerciseLog.setUser(user);

        plannedExerciseLogRepository.save(plannedExerciseLog);
        return "redirect:/planned";
    }

    @PostMapping("/delete-planned/{id}")
    public String deletePlannedExerciseLog(@PathVariable Long id) {
        plannedExerciseLogRepository.deleteById(id);
        return "redirect:/planned";
    }
    
    @GetMapping("/edit-planned/{id}")
    public String showEditPlannedExerciseLogForm(@PathVariable Long id, Model model) {
        Optional<PlannedExerciseLog> plannedExerciseLog = plannedExerciseLogRepository.findById(id);
        if (plannedExerciseLog.isPresent()) {
            model.addAttribute("plannedExerciseLog", plannedExerciseLog.get());
            return "edit-planned";
        } else {
            return "redirect:/planned";
        }
    }

    @PostMapping("/edit-planned/{id}")
    public String updatePlannedExerciseLog(@PathVariable Long id, 
                                        @RequestParam String name, 
                                        @RequestParam String muscleGroup, 
                                        @RequestParam int goalSets, 
                                        @RequestParam int goalReps, 
                                        @RequestParam double goalWeight, 
                                        @RequestParam String notes) {
        Optional<PlannedExerciseLog> plannedExerciseLogOptional = plannedExerciseLogRepository.findById(id);
        if (plannedExerciseLogOptional.isPresent()) {
            PlannedExerciseLog plannedExerciseLog = plannedExerciseLogOptional.get();
            plannedExerciseLog.setPlannedSets(goalSets);
            plannedExerciseLog.setPlannedReps(goalReps);
            plannedExerciseLog.setPlannedWeight(goalWeight);
            plannedExerciseLog.setNotes(notes);

            Exercise exercise = exerciseRepository.findByName(name)
                                .orElse(new Exercise(name, muscleGroup));
            plannedExerciseLog.setExercise(exercise);
            plannedExerciseLogRepository.save(plannedExerciseLog);
        }
        return "redirect:/planned";
    }

}
