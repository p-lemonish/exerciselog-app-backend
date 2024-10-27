package s24.backend.exerciselog.mapper;

import java.util.List;

import org.mapstruct.*;

import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.dto.*;

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

    @Mapping(target = "roleName", source = "role.name")
    UserProfileDto toProfileDto(User entity);

    List<UserProfileDto> toProfileDtoList(List<User> entity);
}
