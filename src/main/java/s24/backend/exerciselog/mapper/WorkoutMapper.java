package s24.backend.exerciselog.mapper;

import java.util.*;
import java.util.stream.Collectors;

import org.mapstruct.*;

import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.dto.*;

@Mapper(componentModel = "spring", uses = {ExerciseLogMapper.class})
public interface WorkoutMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) // Set in @AfterMapping
    @Mapping(target = "date", source = "plannedDate") 
    @Mapping(target = "notes", source = "workoutNotes")
    @Mapping(target = "exerciseLogs", ignore = true)
    @Mapping(target = "plannedExerciseLogs", ignore = true) // Set in service
    @Mapping(target = "name", source = "workoutName")
    Workout toEntity(WorkoutDto dto, @Context User user);

    @AfterMapping
    default void setUser(@MappingTarget Workout entity, @Context User user) {
        entity.setUser(user);
    }

    @Mapping(target = "workoutName", source = "name")
    @Mapping(target = "workoutNotes", source = "notes")
    @Mapping(target = "plannedDate", source = "date")
    @Mapping(target = "selectedExerciseIds", expression = "java(mapPlannedExerciseLogsToIds(entity.getPlannedExerciseLogs()))") // Set in @Aftermapping
    WorkoutDto toDto(Workout entity);

    List<WorkoutDto> toDtoList(List<Workout> entities);

    @AfterMapping
    default List<Long> mapPlannedExerciseLogsToIds(List<PlannedExerciseLog> plannedExerciseLogs) {
        if(plannedExerciseLogs == null) {
            return null;
        }
        return plannedExerciseLogs.stream()
            .map(PlannedExerciseLog::getId)
            .collect(Collectors.toList());
    }
}