package s24.backend.exerciselog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import s24.backend.exerciselog.domain.SetLog;
import s24.backend.exerciselog.dto.SetLogDto;

@Mapper(componentModel = "spring")
public interface SetLogMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "exerciseLog", ignore = true)
    SetLog toEntity(SetLogDto dto);

    SetLogDto tDto(SetLog entity);
}
