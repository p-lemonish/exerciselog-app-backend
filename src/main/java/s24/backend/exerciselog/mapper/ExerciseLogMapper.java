package s24.backend.exerciselog.mapper;

import org.mapstruct.*;

import java.util.*;

import s24.backend.exerciselog.domain.dto.*;
import s24.backend.exerciselog.domain.entity.ExerciseLog;
import s24.backend.exerciselog.domain.entity.PlannedExerciseLog;
import s24.backend.exerciselog.domain.entity.SetLog;
import s24.backend.exerciselog.domain.entity.User;
import s24.backend.exerciselog.domain.entity.Workout;

@Mapper(componentModel = "spring", uses = {SetLogMapper.class})
public interface ExerciseLogMapper {

    @Mapping(target = "date", ignore = true) // Set in WorkoutService completeWorkout
    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "exercise", source = "plannedExerciseLog.exercise")
    @Mapping(target = "plannedExerciseLog", source = "plannedExerciseLog")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "workout", source = "workout")
    @Mapping(target = "setLogs", source = "dto.setLogDtoList")
    @Mapping(target = "name", source = "dto.exerciseName")
    @Mapping(target = "notes", source = "dto.exerciseNotes")
    ExerciseLog toEntity(
        ExerciseLogDto dto,
        @MappingTarget ExerciseLog entity,
        PlannedExerciseLog plannedExerciseLog,
        User user,
        Workout workout
    );

    @Mapping(target = "id", source = "id")
    @Mapping(target = "exerciseId", source = "exercise.id")
    @Mapping(target = "exerciseName", source = "name")
    @Mapping(target = "exerciseNotes", source = "notes")
    @Mapping(target = "setLogDtoList", source = "setLogs")
    @Mapping(target = "date", source = "date")
    ExerciseLogDto toDto(ExerciseLog entity);

    List<ExerciseLogDto> toDtoList(List<ExerciseLog> entities);

    @AfterMapping
    default void setExerciseLogRelationships(@MappingTarget ExerciseLog entity) {
        if (entity.getSetLogs() != null) {
            for (SetLog setLog : entity.getSetLogs()) {
                setLog.setExerciseLog(entity);
            }
        }
    }

    @Mapping(target = "exerciseId", source = "id")
    @Mapping(target = "exerciseName", source = "exercise.name")
    @Mapping(target = "exerciseNotes", source = "notes")
    @Mapping(target = "setLogDtoList", expression = "java(generateSetLogDtoList(plannedExerciseLog))")
    @Mapping(target = "date", expression = "java(java.time.LocalDate.now())")
    ExerciseLogDto plannedExerciseLogToDto(PlannedExerciseLog plannedExerciseLog);

    List<ExerciseLogDto> plannedExerciseLogListToDtoList(List<PlannedExerciseLog> plannedExerciseLogs);

    /*
    * SetLogDtos are created and pre-filled
    * upon creation of ExerciseLogDto from a PlannedExerciseLog
    * -> User is about to fill their workout data based on plans
    */
    default List<SetLogDto> generateSetLogDtoList(PlannedExerciseLog plannedExerciseLog) {
        List<SetLogDto> setLogDtos = new ArrayList<>();
        int plannedSets = plannedExerciseLog.getPlannedSets();
        int plannedReps = plannedExerciseLog.getPlannedReps();
        double plannedWeight = plannedExerciseLog.getPlannedWeight();

        for (int i = 1; i <= plannedSets; i++) {
            SetLogDto setLogDto = new SetLogDto();
            setLogDto.setSetNumber(i);
            setLogDto.setReps(plannedReps);
            setLogDto.setWeight(plannedWeight);
            setLogDtos.add(setLogDto);
        }
        return setLogDtos;
    }
}