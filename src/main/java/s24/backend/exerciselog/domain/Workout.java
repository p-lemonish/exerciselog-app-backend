package s24.backend.exerciselog.domain;

import java.time.LocalDate;
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
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL)
    private List<ExerciseLog> exerciseLog;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL)
    private List<PlannedExercise> plannedExercises;
    
    private String name;
    private String notes;
    private LocalDate date;

    public Workout(User user, List<ExerciseLog> exerciseLog, List<PlannedExercise> plannedExercises, String name,
            String notes, LocalDate date) {
        this.user = user;
        this.exerciseLog = exerciseLog;
        this.plannedExercises = plannedExercises;
        this.name = name;
        this.notes = notes;
        this.date = date;
    }
    public Workout() {
    }
    public List<PlannedExercise> getPlannedExercises() {
        return plannedExercises;
    }
    public void setPlannedExercises(List<PlannedExercise> plannedExercises) {
        this.plannedExercises = plannedExercises;
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
