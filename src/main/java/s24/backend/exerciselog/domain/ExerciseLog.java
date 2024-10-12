package s24.backend.exerciselog.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class ExerciseLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String notes;

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

    @OneToMany(mappedBy = "exerciseLog", cascade = CascadeType.ALL)
    private List<SetLog> setLogs = new ArrayList<>();

    public ExerciseLog() {
    }
    public ExerciseLog(String name, String notes, Exercise exercise, Workout workout) {
        this.name = name;
        this.notes = notes;
        this.exercise = exercise;
        this.workout = workout;
    }
    public List<SetLog> getSetLogs() {
        return setLogs;
    }
    public void setSetLogs(List<SetLog> setLogs) {
        this.setLogs = setLogs;
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
