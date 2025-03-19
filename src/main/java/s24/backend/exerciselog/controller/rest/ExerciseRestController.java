package s24.backend.exerciselog.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import s24.backend.exerciselog.domain.dto.ExerciseDto;
import s24.backend.exerciselog.domain.entity.User;
import s24.backend.exerciselog.service.PlannedExerciseLogService;
import s24.backend.exerciselog.util.SecurityUtils;

@RestController
public class ExerciseRestController {
    @Autowired
    private PlannedExerciseLogService plannedExerciseLogService;

    @GetMapping("/api/exercises")
    public ResponseEntity<List<ExerciseDto>> getUserExercises() {
        User user = SecurityUtils.getCurrentUser();
        List<ExerciseDto> userExercises = plannedExerciseLogService.getUserExercises(user);
        return ResponseEntity.status(HttpStatus.OK).body(userExercises);
    }
}
