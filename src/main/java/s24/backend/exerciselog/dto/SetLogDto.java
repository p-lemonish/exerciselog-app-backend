package s24.backend.exerciselog.dto;

import jakarta.validation.constraints.Min;

/*
 * int setNumber
 * int reps
 * double weight
 */

public class SetLogDto {
    private int setNumber;
    @Min(value = 0, message = "Reps must be non-negative")
    private int reps;
    @Min(value = 0, message = "Weight must be non-negative")
    private double weight;
    public SetLogDto() {
    }
    public int getSetNumber() {
        return setNumber;
    }
    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
    }
    public int getReps() {
        return reps;
    }
    public void setReps(int reps) {
        this.reps = reps;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
}
