package s24.backend.exerciselog.mapper;

import org.mapstruct.*;

import s24.backend.exerciselog.domain.User;
import s24.backend.exerciselog.dto.UserRegistrationDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "workouts", ignore = true)
    @Mapping(target = "completedWorkouts", ignore = true)
    @Mapping(target = "plannedExerciseLogs", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(UserRegistrationDto dto);

    @Mapping(target = "confirmPassword", ignore = true)
    UserRegistrationDto toDto(User entity);
}
