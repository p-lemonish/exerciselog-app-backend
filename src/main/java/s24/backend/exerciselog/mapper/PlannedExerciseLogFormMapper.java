package s24.backend.exerciselog.mapper;

import org.mapstruct.*;

import java.util.*;

import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.dto.*;

@Mapper(componentModel = "spring")
public interface PlannedExerciseLogFormMapper {

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "exerciseName", target = "exercise.name")
    PlannedExerciseLog toPlannedExerciseLog(PlannedExerciseLogForm plannedExerciseLogForm);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "exerciseName", source = "exercise.name")
    PlannedExerciseLogForm toDto(PlannedExerciseLog plannedExerciseLog);

    List<PlannedExerciseLog> toPlannedExerciseLogs(List<PlannedExerciseLogForm> plannedExerciseLogForms);
    List<PlannedExerciseLogForm> toDtos(List<PlannedExerciseLog> plannedExerciseLogs);
}