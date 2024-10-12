package s24.backend.exerciselog.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ExerciseLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String notes;
    private int sets;
    private int reps;
    private double weight;
    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @ManyToOne
    @JoinColumn(name = "workout_id")
    private Workout workout;

    @ManyToOne
    @JoinColumn(name = "completed_workout_id")
    private CompletedWorkout completedWorkout;

    @ManyToOne
    @JoinColumn(name = "planned_exercise_log_id")
    private PlannedExerciseLog plannedExerciseLog;

    public ExerciseLog() {
    }
    public ExerciseLog(String name, String notes, int sets, int reps, double weight, Exercise exercise,
            Workout workout) {
        this.name = name;
        this.notes = notes;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.exercise = exercise;
        this.workout = workout;
    }
    public PlannedExerciseLog getPlannedExerciseLog() {
        return plannedExerciseLog;
    }
    public void setPlannedExerciseLog(PlannedExerciseLog plannedExerciseLog) {
        this.plannedExerciseLog = plannedExerciseLog;
    }
    public CompletedWorkout getCompletedWorkout() {
        return completedWorkout;
    }
    public void setCompletedWorkout(CompletedWorkout completedWorkout) {
        this.completedWorkout = completedWorkout;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public int getSets() {
        return sets;
    }
    public void setSets(int sets) {
        this.sets = sets;
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
}
