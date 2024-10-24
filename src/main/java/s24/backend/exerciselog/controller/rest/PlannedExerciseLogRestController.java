package s24.backend.exerciselog.controller.rest;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import s24.backend.exerciselog.domain.User;
import s24.backend.exerciselog.dto.*;
import s24.backend.exerciselog.service.PlannedExerciseLogService;
import s24.backend.exerciselog.util.SecurityUtils;

@RestController
public class PlannedExerciseLogRestController {
    @Autowired
    private PlannedExerciseLogService plannedExerciseLogService;
    
    @GetMapping("/api/planned")
    public ResponseEntity<List<PlannedExerciseLogDto>> getAllPlannedExerciseLogs() {
        User user = SecurityUtils.getCurrentUser();
        List<PlannedExerciseLogDto> plannedExerciseLogs = plannedExerciseLogService.getAllPlannedExerciseLogs(user);
        return ResponseEntity.ok(plannedExerciseLogs);
    }

    @GetMapping("/api/planned/{id}")
    public ResponseEntity<String> getPlannedExerciseLog(@RequestParam String param) {
        //TODO
        return null;
    }
    
    @PostMapping("/api/planned")
    public ResponseEntity<String> addPlannedExerciseLog(@RequestBody String entity) {
        //TODO: process POST request
        
        return null;
    }

    @DeleteMapping("/api/planned/{id}")
    public ResponseEntity<String> deletePlannedExerciseLog(@PathVariable Long id) {
        //TODO
        return null;
    }

    @PutMapping("/api/planned/{id}")
    public ResponseEntity<String> editPlannedExerciseLog(@PathVariable String id, @RequestBody String entity) {
        //TODO: process PUT request
        
        return null;
    }
    
}
