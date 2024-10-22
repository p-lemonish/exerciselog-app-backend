package s24.backend.exerciselog.mapper;

import org.mapstruct.*;

import java.util.*;

import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.dto.*;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {
    @Mapping(target = "id", source = "exerciseId")
    @Mapping(target = "user", ignore = true) // Set in service or after mapping if needed
    @Mapping(target = "exerciseLogs", ignore = true)
    @Mapping(target = "plannedExerciseLogs", ignore = true)
    Exercise toEntity(ExerciseDto dto);
    List<Exercise> toEntityList(List<ExerciseDto> dtos);

    @Mapping(target = "exerciseId", source = "id")
    @Mapping(target = "userId", source = "user.id")
    ExerciseDto toDto(Exercise exercise);
    List<ExerciseDto> toDtoList(List<Exercise> exercises);
}