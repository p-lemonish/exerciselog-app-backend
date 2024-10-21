package s24.backend.exerciselog.mapper;

import org.mapstruct.*;

import java.util.*;

import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.dto.*;

@Mapper(componentModel = "spring")
public interface ExerciseLogMapper {

    @Mapping(target = "name", source = "plannedExerciseLog.exercise.name")
    @Mapping(target = "exercise", source = "plannedExerciseLog.exercise")
    @Mapping(target = "plannedExerciseLog", source = "plannedExerciseLog")
    @Mapping(target = "notes", source = "exerciseLogDto.exerciseNotes")
    @Mapping(target = "user", source = "currentUser")
    @Mapping(target = "workout", source = "workout")
    @Mapping(target = "completedWorkout", source = "completedWorkout")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "setLogs", ignore = true) // manual mapping done 
    ExerciseLog toExerciseLog(ExerciseLogDto exerciseLogDto, PlannedExerciseLog plannedExerciseLog, Workout workout, User currentUser, CompletedWorkout completedWorkout);

    @Mapping(target = "exerciseLog", source = "exerciseLog")
    SetLog toSetLog(SetLogDto setLogDto, ExerciseLog exerciseLog);

    @Mapping(target = "exerciseId", source = "exercise.id")
    @Mapping(target = "exerciseName", source = "name")
    @Mapping(target = "exerciseNotes", source = "notes")
    @Mapping(target = "setData", source = "setLogs")
    @Mapping(target = "date", source = "completedWorkout.date")
    ExerciseLogDto toExerciseLogDtoFromExerciseLog(ExerciseLog exerciseLog);
    @Named("toExerciseLogDtoListFromExerciseLogs")
    List<ExerciseLogDto> toExerciseLogDtoListFromExerciseLogs(List<ExerciseLog> exerciseLogs);

    @Mapping(target = "exerciseId", source = "plannedExerciseLog.id")
    @Mapping(target = "exerciseName", source = "plannedExerciseLog.exercise.name")
    @Mapping(target = "setData", expression = "java(mapSetLogDto(plannedExerciseLog))")
    @Mapping(target = "date", expression = "java(java.time.LocalDate.now())") // date of completion
    @Mapping(target = "exerciseNotes", ignore = true)
    ExerciseLogDto toExerciseLogDtoFromPlannedExerciseLog(PlannedExerciseLog plannedExerciseLog);
    List<ExerciseLogDto> toExerciseLogDtoListFromPlannedExerciseLogs(List<PlannedExerciseLog> plannedExerciseLogs);

    SetLogDto toSetLogDto(SetLog setLog);
    List<SetLogDto> toSetLogDtoList(List<SetLog> setLogs);

    default List<SetLogDto> mapSetLogDto(PlannedExerciseLog plannedExerciseLog) {
        List <SetLogDto> sets = new ArrayList<>();
            for(int i = 1; i <= plannedExerciseLog.getPlannedSets(); i++) {
                SetLogDto setLogDto = new SetLogDto();
                setLogDto.setSetNumber(i);
                setLogDto.setReps(plannedExerciseLog.getPlannedReps());
                setLogDto.setWeight(plannedExerciseLog.getPlannedWeight());
                sets.add(setLogDto);
            }
            return sets;
    }
}