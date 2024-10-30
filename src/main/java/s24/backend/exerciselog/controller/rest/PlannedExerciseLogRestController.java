package s24.backend.exerciselog.controller.rest;

import java.util.*;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import s24.backend.exerciselog.domain.dto.*;
import s24.backend.exerciselog.domain.entity.Exercise;
import s24.backend.exerciselog.domain.entity.User;
import s24.backend.exerciselog.service.PlannedExerciseLogService;
import s24.backend.exerciselog.util.SecurityUtils;
import s24.backend.exerciselog.util.ValidationUtil;

@RestController
@RequestMapping("/api/planned")
public class PlannedExerciseLogRestController {
    @Autowired
    private PlannedExerciseLogService plannedExerciseLogService;
    
    @GetMapping
    public ResponseEntity<List<PlannedExerciseLogDto>> getAllPlannedExerciseLogs() {
        User user = SecurityUtils.getCurrentUser();
        List<PlannedExerciseLogDto> plannedExerciseLogs = plannedExerciseLogService.getAllPlannedExerciseLogs(user);
        return ResponseEntity.ok(plannedExerciseLogs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlannedExerciseLogDto> getPlannedExerciseLog(@PathVariable Long id) {
        PlannedExerciseLogDto plannedExerciseLogDto = plannedExerciseLogService.getPlannedExerciseLogDtoById(id);
        return ResponseEntity.ok(plannedExerciseLogDto);
    }
    
    @PostMapping
    public ResponseEntity<?> addPlannedExerciseLog(
        @Valid @RequestBody PlannedExerciseLogDto plannedExerciseLogDto, BindingResult result) throws BadRequestException {

        ResponseEntity<Map<String, String>> validationErrors = ValidationUtil.handleValidationErrors(result);
        if(validationErrors != null) {
            return validationErrors;
        }
        
        Exercise exercise = plannedExerciseLogService.findOrCreateExercise(plannedExerciseLogDto);
        plannedExerciseLogService.addPlannedExerciseLog(plannedExerciseLogDto, exercise);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlannedExerciseLog(@PathVariable Long id) throws BadRequestException {
        plannedExerciseLogService.deletePlannedExerciseLog(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editPlannedExerciseLog(
        @PathVariable Long id,
        @Valid @RequestBody PlannedExerciseLogDto plannedExerciseLogDto, BindingResult result) throws BadRequestException {

        ResponseEntity<Map<String, String>> validationErrors = ValidationUtil.handleValidationErrors(result);
        if(validationErrors != null) {
            return validationErrors;
        }

        plannedExerciseLogDto.setId(id);
        plannedExerciseLogService.updatePlannedExerciseLog(plannedExerciseLogDto);

        return ResponseEntity.ok().build();
    }
    
}
