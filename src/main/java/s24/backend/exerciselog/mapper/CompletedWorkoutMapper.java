package s24.backend.exerciselog.mapper;

import org.mapstruct.*;
import java.util.*;

import s24.backend.exerciselog.domain.CompletedWorkout;
import s24.backend.exerciselog.dto.CompletedWorkoutDto;

@Mapper(componentModel = "spring", uses = {ExerciseLogMapper.class})
public interface CompletedWorkoutMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "exerciseLogDtos", source = "exerciseLogs", qualifiedByName = "toExerciseLogDtoListFromExerciseLogs")
    CompletedWorkoutDto toCompletedWorkoutDto(CompletedWorkout completedWorkout);
    List<CompletedWorkoutDto> toCompletedWorkoutDtos(List<CompletedWorkout> completedWorkouts);
}
