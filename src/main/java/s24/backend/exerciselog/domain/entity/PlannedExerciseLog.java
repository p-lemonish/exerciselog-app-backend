package s24.backend.exerciselog.domain.entity;

import jakarta.persistence.*;
import s24.backend.exerciselog.util.AttributeEncryptor;

@Entity
public class PlannedExerciseLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int plannedReps;
    private int plannedSets;
    private double plannedWeight;
    @Convert(converter = AttributeEncryptor.class)
    private String notes;

    public PlannedExerciseLog(Exercise exercise, User user, int plannedSets, int plannedReps, double plannedWeight,
            String notes) {
        this.exercise = exercise;
        this.user = user;
        this.plannedReps = plannedReps;
        this.plannedSets = plannedSets;
        this.plannedWeight = plannedWeight;
        this.notes = notes;
    }
    public PlannedExerciseLog() {}
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
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
