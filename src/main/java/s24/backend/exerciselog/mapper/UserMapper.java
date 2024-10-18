package s24.backend.exerciselog.mapper;

import org.mapstruct.Mapper;

import s24.backend.exerciselog.domain.User;
import s24.backend.exerciselog.dto.UserRegistrationDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRegistrationDto userRegistrationDto);
    UserRegistrationDto toDto(User user);
}
