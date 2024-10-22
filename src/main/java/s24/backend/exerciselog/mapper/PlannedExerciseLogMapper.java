package s24.backend.exerciselog.mapper;

import org.mapstruct.*;

import java.util.*;

import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.dto.*;

@Mapper(componentModel = "spring")
public interface PlannedExerciseLogMapper {

    @Mapping(target = "exerciseName", source = "exercise.name")
    @Mapping(target = "muscleGroup", source = "exercise.muscleGroup")
    @Mapping(target = "userId", source = "user.id")
    PlannedExerciseLogDto toDto(PlannedExerciseLog entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "exercise", ignore = true) // Will be set after mapping
    @Mapping(target = "user", ignore = true) // Will be set after mapping
    PlannedExerciseLog toEntity(PlannedExerciseLogDto dto, @Context User user, @Context Exercise exercise);

    @AfterMapping
    default void setUserAndExercise(@MappingTarget PlannedExerciseLog entity, @Context User user, @Context Exercise exercise) {
        entity.setUser(user);
        entity.setExercise(exercise);
    }

    List<PlannedExerciseLogDto> toDtoList(List<PlannedExerciseLog> entities);
}
