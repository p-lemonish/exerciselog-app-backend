package s24.backend.exerciselog.mapper;

import java.util.*;
import org.mapstruct.*;

import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.dto.*;

@Mapper(componentModel = "spring")
public interface WorkoutMapper {

    @Mapping(target = "workoutName", source = "workout.name")
    @Mapping(target = "workoutNotes", source = "workout.notes")
    @Mapping(target = "plannedDate", source = "workout.date") // date of planned workout
    @Mapping(target = "date", expression = "java(java.time.LocalDate.now())") // date of completion
    @Mapping(target = "user", source = "currentUser")
    @Mapping(target = "notes", source = "workoutDto.workoutNotes")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "exerciseLogs", ignore = true)
    CompletedWorkout toCompletedWorkout(WorkoutDto workoutDto, Workout workout, User currentUser);

    @Mapping(target = "workoutNotes", source = "workout.notes")
    @Mapping(target= "workoutName", source = "workout.name")
    @Mapping(target = "plannedDate", source = "workout.date") // date of planned workout
    @Mapping(target = "date", expression = "java(java.time.LocalDate.now())") // date of completion
    @Mapping(target = "exercises", ignore = true)
    WorkoutDto toWorkoutDto(Workout workout);
    List<WorkoutDto> toWorkoutDtos(List<Workout> workouts);
}