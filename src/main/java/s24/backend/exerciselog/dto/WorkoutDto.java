package s24.backend.exerciselog.dto;

import java.util.List;
import java.time.LocalDate;

import jakarta.validation.Valid;

/*
 * Long id => workoutId
 * String workoutNotes
 * String workoutName
 * LocalDate plannedDate
 * LocalDate date
 * List<Long> selectedExerciseIds => for selecting exercises when creating a new workout (addWorkout)
 * List<ExerciseLogDto> exercises
 *     - Long exerciseId
 *     - List<SetLogDto> setLogDtoList 
 *            - int setNumber
 *            - int reps
 *            - double weight
 *     - String exerciseNotes
 *     - String exerciseName
 *     - LocalDate date => date of completion
 */

public class WorkoutDto {
    
    private Long id;
    private String workoutNotes;
    private List<Long> selectedExerciseIds;
    @Valid
    private List<ExerciseLogDto> exercises;
    private String workoutName;
    private LocalDate plannedDate; //date the workout was planned for
    private LocalDate date; //workout completion date
    public WorkoutDto() {}
    public List<Long> getSelectedExerciseIds() {
        return selectedExerciseIds;
    }
    public void setSelectedExerciseIds(List<Long> selectedExerciseIds) {
        this.selectedExerciseIds = selectedExerciseIds;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getWorkoutName() {
        return workoutName;
    }
    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }
    public LocalDate getPlannedDate() {
        return plannedDate;
    }
    public void setPlannedDate(LocalDate plannedDate) {
        this.plannedDate = plannedDate;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public String getWorkoutNotes() {
        return workoutNotes;
    }
    public void setWorkoutNotes(String workoutNotes) {
        this.workoutNotes = workoutNotes;
    }
    public List<ExerciseLogDto> getExercises() {
        return exercises;
    }
    public void setExercises(List<ExerciseLogDto> exercises) {
        this.exercises = exercises;
    }
}
