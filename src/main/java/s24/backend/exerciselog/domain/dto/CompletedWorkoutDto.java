package s24.backend.exerciselog.domain.dto;

import java.time.LocalDate;
import java.util.*;

import jakarta.validation.Valid;

public class CompletedWorkoutDto {
    private Long id;
    private Long userId;

    private String workoutName;
    private String workoutNotes;

    private LocalDate date; // completion date
    private LocalDate plannedDate; // date for which workout was planned for

    @Valid
    private List<ExerciseLogDto> exercises;

    public CompletedWorkoutDto() {}
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getWorkoutName() {
        return workoutName;
    }
    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }
    public String getWorkoutNotes() {
        return workoutNotes;
    }
    public void setWorkoutNotes(String workoutNotes) {
        this.workoutNotes = workoutNotes;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public LocalDate getPlannedDate() {
        return plannedDate;
    }
    public void setPlannedDate(LocalDate plannedDate) {
        this.plannedDate = plannedDate;
    }
    public List<ExerciseLogDto> getExercises() {
        return exercises;
    }
    public void setExercises(List<ExerciseLogDto> exercises) {
        this.exercises = exercises;
    }
}
