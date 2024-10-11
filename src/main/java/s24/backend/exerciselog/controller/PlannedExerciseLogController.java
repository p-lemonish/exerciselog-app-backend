package s24.backend.exerciselog.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import s24.backend.exerciselog.domain.Exercise;
import s24.backend.exerciselog.domain.PlannedExerciseLog;
import s24.backend.exerciselog.repository.ExerciseRepository;
import s24.backend.exerciselog.repository.PlannedExerciseLogRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PlannedExerciseLogController {
    @Autowired
    private PlannedExerciseLogRepository plannedExerciseLogsRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @GetMapping("/planned")
    public String getAllPlannedExerciseLogs(Model model) {
        model.addAttribute("plannedExerciseLogs", plannedExerciseLogsRepository.findAll());
        model.addAttribute("exercises", exerciseRepository.findAll());
        return "planned";
    }

    @PostMapping("/add-planned-exercise-log")
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

        PlannedExerciseLog plannedExerciseLog = new PlannedExerciseLog(exercise, goalSets, goalReps, goalWeight, notes);
        plannedExerciseLogsRepository.save(plannedExerciseLog);
        return "redirect:/planned";
    }
    //TODO PUT and DELETE mapping
    
}
