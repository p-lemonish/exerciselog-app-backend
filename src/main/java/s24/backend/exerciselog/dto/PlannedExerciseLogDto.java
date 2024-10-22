package s24.backend.exerciselog.dto;

import jakarta.validation.constraints.*;

/*
 * Long id => PlannedExerciseLog id
 * Long userId
 * String exerciseName
 * String muscleGroup
 * String notes
 * int plannedSets
 * int plannedReps
 * double plannedWeight
 */

public class PlannedExerciseLogDto {
    private Long id;

    @NotEmpty(message = "Exercise must have a name")
    private String exerciseName;
    private String muscleGroup;

    private Long userId;

    @Min(value = 1, message = "Reps must be a number greater than 0")
    private int plannedReps;

    @Min(value = 1, message = "Sets must be a number greater than 0")
    private int plannedSets;

    @Min(value = 0, message = "Weight must be a number greater than 0")
    private double plannedWeight;

    private String notes;

    public PlannedExerciseLogDto() {}
    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getPlannedReps() {
        return plannedReps;
    }

    public void setPlannedReps(int plannedReps) {
        this.plannedReps = plannedReps;
    }

    public int getPlannedSets() {
        return plannedSets;
    }

    public void setPlannedSets(int plannedSets) {
        this.plannedSets = plannedSets;
    }

    public double getPlannedWeight() {
        return plannedWeight;
    }

    public void setPlannedWeight(double plannedWeight) {
        this.plannedWeight = plannedWeight;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMuscleGroup() {
        return muscleGroup;
    }
    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }
}
