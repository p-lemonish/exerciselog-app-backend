package s24.backend.exerciselog.dto;

public class SetData {
    private int setNumber;
    private int reps;
    private double weight;
    public SetData() {
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
