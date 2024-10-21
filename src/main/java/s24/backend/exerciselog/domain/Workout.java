package s24.backend.exerciselog.domain;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "workout")
    private List<ExerciseLog> exerciseLog;

    @ManyToMany
    @JoinTable(
        name = "workout_planned_exercises",
        joinColumns = @JoinColumn(name = "workout_id"),
        inverseJoinColumns = @JoinColumn(name = "planned_exercise_logs_id")
    )
    private List<PlannedExerciseLog> plannedExerciseLogs;
    
    private String name;
    private String notes;
    private LocalDate date;

    public Workout(User user, List<PlannedExerciseLog> plannedExerciseLogs, String name, String notes, LocalDate date) {
        this.user = user;
        this.plannedExerciseLogs = plannedExerciseLogs;
        this.name = name;
        this.notes = notes;
        this.date = date;
    }
    public Workout() {
    }
    public List<PlannedExerciseLog> getPlannedExerciseLogs() {
        return plannedExerciseLogs;
    }
    public void setPlannedExercises(List<PlannedExerciseLog> plannedExerciseLogs) {
        this.plannedExerciseLogs = plannedExerciseLogs;
    }
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
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public List<ExerciseLog> getExerciseLog() {
        return exerciseLog;
    }
    public void setExerciseLog(List<ExerciseLog> exerciseLog) {
        this.exerciseLog = exerciseLog;
    }
}
