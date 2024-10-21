package s24.backend.exerciselog.mapper;

import org.mapstruct.*;

import java.util.*;

import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.dto.*;

@Mapper(componentModel = "spring")
public interface PlannedExerciseLogMapper {

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "exerciseName", target = "exercise.name")
    PlannedExerciseLog toPlannedExerciseLog(PlannedExerciseLogDto plannedExerciseLogForm);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "exerciseName", source = "exercise.name")
    PlannedExerciseLogDto toDto(PlannedExerciseLog plannedExerciseLog);

    List<PlannedExerciseLog> toPlannedExerciseLogs(List<PlannedExerciseLogDto> plannedExerciseLogForms);
    List<PlannedExerciseLogDto> toDtos(List<PlannedExerciseLog> plannedExerciseLogs);
}