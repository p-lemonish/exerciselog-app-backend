package s24.backend.exerciselog.mapper;

import java.util.*;
import org.mapstruct.*;

import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.dto.*;

@Mapper(componentModel = "spring", uses = {ExerciseLogMapper.class})
public interface WorkoutMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) // Set in @AfterMapping
    @Mapping(target = "date", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "notes", source = "workoutNotes")
    @Mapping(target = "exerciseLogs", ignore = true)
    @Mapping(target = "plannedExerciseLogs", ignore = true)
    @Mapping(target = "name", source = "workoutName")
    Workout toEntity(WorkoutDto dto, @Context User user);

    @AfterMapping
    default void setUser(@MappingTarget CompletedWorkout entity, @Context User user) {
        entity.setUser(user);
    }

    @Mapping(target = "workoutName", source = "name")
    @Mapping(target = "workoutNotes", source = "notes")
    @Mapping(target = "plannedDate", source = "date")
    @Mapping(target = "exercises", ignore = true) // Will be set manually (WorkoutService -> startWorkout)
    WorkoutDto toDto(Workout entity);

    List<WorkoutDto> toDtoList(List<Workout> entities);
}