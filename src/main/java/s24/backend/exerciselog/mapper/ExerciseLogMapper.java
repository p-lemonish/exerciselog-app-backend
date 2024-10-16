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
    @Mapping(target = "notes", source = "dto.exerciseNotes")
    @Mapping(target = "user", source = "currentUser")
    @Mapping(target = "workout", source = "workout")
    @Mapping(target = "completedWorkout", source = "completedWorkout")
    @Mapping(target = "id", ignore = true)
    ExerciseLog toExerciseLog(ExerciseCompletionData dto, PlannedExerciseLog plannedExerciseLog, Workout workout, User currentUser, CompletedWorkout completedWorkout);

    @Mapping(target = "exerciseLog", source = "exerciseLog")
    SetLog toSetLog(SetData setData, ExerciseLog exerciseLog);

    @Mapping(target = "exerciseId", source = "exercise.id")
    @Mapping(target = "exerciseName", source = "name")
    @Mapping(target = "exerciseNotes", source = "notes")
    @Mapping(target = "setData", source = "setLogs")
    @Mapping(target = "date", source = "completedWorkout.date")
    ExerciseCompletionData toExerciseCompletionData(ExerciseLog exerciseLog);

    List<ExerciseCompletionData> toExerciseCompletionDataList(List<ExerciseLog> exerciseLogs);

    SetData toSetData(SetLog setLog);

    List<SetData> toSetDataList(List<SetLog> setLogs);
}

