package s24.backend.exerciselog.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.dto.*;
import s24.backend.exerciselog.mapper.ExerciseLogMapper;
import s24.backend.exerciselog.repository.ExerciseLogRepository;
import s24.backend.exerciselog.util.SecurityUtils;

@RestController
public class ExerciseLogRestController {
    @Autowired
    private ExerciseLogRepository exerciseLogRepository;
    @Autowired
    private ExerciseLogMapper exerciseLogMapper;
    
    @GetMapping("/api/logs")
    public ResponseEntity<List<ExerciseLogDto>> showExerciseLogs(
        @RequestParam(value = "exerciseName", required = false) String exerciseName) {

        User user = SecurityUtils.getCurrentUser();
        List<ExerciseLog> exerciseLogs;
        if(exerciseName != null && !exerciseName.isEmpty()) {
            exerciseLogs = exerciseLogRepository.findByUserAndNameIgnoreCaseOrderByDate(user, exerciseName);
        } else {
            exerciseLogs = exerciseLogRepository.findByUserOrderByDate(user);
        }
        List<ExerciseLogDto> exerciseLogDtoList = exerciseLogMapper.toDtoList(exerciseLogs);

        return ResponseEntity.status(HttpStatus.OK).body(exerciseLogDtoList);
    }
    
}
