package s24.backend.exerciselog.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

//Compare List of this vs Workout's List of ExerciseLogs
@Entity
public class PlannedExerciseLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @ManyToOne
    @JoinColumn(name = "workout_id")
    private Workout workout;

    private int plannedReps;
    private int plannedSets;
    private double plannedWeight;
    private String notes;
    public PlannedExerciseLog() {
    }
    public PlannedExerciseLog(Exercise exercise, Workout workout, int plannedReps, int plannedSets, double plannedWeight,
            String notes) {
        this.exercise = exercise;
        this.workout = workout;
        this.plannedReps = plannedReps;
        this.plannedSets = plannedSets;
        this.plannedWeight = plannedWeight;
        this.notes = notes;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Exercise getExercise() {
        return exercise;
    }
    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
    public Workout getWorkout() {
        return workout;
    }
    public void setWorkout(Workout workout) {
        this.workout = workout;
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
}
