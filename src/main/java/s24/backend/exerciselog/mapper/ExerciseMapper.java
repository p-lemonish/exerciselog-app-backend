package s24.backend.exerciselog.mapper;

import org.mapstruct.*;

import java.util.*;

import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.dto.*;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {
    @Mapping(source = "exerciseId", target = "id")
    @Mapping(source = "userId", target = "user.id")
    Exercise toExercise(ExerciseDto dto);
    List<Exercise> toExercises(List<ExerciseDto> dtos);

    @Mapping(target = "exerciseId", source = "id")
    @Mapping(target = "userId", source = "user.id")
    ExerciseDto toDto(Exercise exercise);
    List<ExerciseDto> toDtos(List<Exercise> exercises);
}