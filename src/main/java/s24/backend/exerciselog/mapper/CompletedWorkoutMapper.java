package s24.backend.exerciselog.mapper;

import org.mapstruct.*;
import java.util.*;

import s24.backend.exerciselog.domain.dto.CompletedWorkoutDto;
import s24.backend.exerciselog.domain.entity.CompletedWorkout;

@Mapper(componentModel = "spring", uses = {ExerciseLogMapper.class})
public interface CompletedWorkoutMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) // Will be set in service (completeWorkout) along with most other relevant context
    @Mapping(target = "exerciseLogs", ignore = true)
    CompletedWorkout toEntity(CompletedWorkoutDto dto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "exercises", source = "exerciseLogs")
    CompletedWorkoutDto toDto(CompletedWorkout entity);

    List<CompletedWorkoutDto> toDtoList(List<CompletedWorkout> entities);
}
