package s24.backend.exerciselog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import s24.backend.exerciselog.domain.dto.SetLogDto;
import s24.backend.exerciselog.domain.entity.SetLog;

@Mapper(componentModel = "spring")
public interface SetLogMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "exerciseLog", ignore = true)
    SetLog toEntity(SetLogDto dto);

    SetLogDto tDto(SetLog entity);
}
