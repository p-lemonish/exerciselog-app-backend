package s24.backend.exerciselog.domain.entity;

import jakarta.persistence.*;
import s24.backend.exerciselog.util.AttributeEncryptor;

import java.time.LocalDate;
import java.util.List;

@Entity
public class CompletedWorkout {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate date; // completion date
    @Convert(converter = AttributeEncryptor.class)
    private String workoutName;
    @Convert(converter = AttributeEncryptor.class)
    private String workoutNotes;

    @OneToMany
    private List<ExerciseLog> exerciseLogs;

    public CompletedWorkout(User user, LocalDate date, List<ExerciseLog> exerciseLogs) {
        this.user = user;
        this.date = date;
        this.exerciseLogs = exerciseLogs;
    }

    public CompletedWorkout() {
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public String getWorkoutNotes() {
        return workoutNotes;
    }

    public void setWorkoutNotes(String workoutNotes) {
        this.workoutNotes = workoutNotes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<ExerciseLog> getExerciseLogs() {
        return exerciseLogs;
    }

    public void setExerciseLogs(List<ExerciseLog> exerciseLogs) {
        this.exerciseLogs = exerciseLogs;
    }
    /*
     * disabled for now until I find a good used for this
     * private LocalDate plannedDate; //date the workout was planned for
     * public LocalDate getPlannedDate() {
     * return plannedDate;
     * }
     * public void setPlannedDate(LocalDate plannedDate) {
     * this.plannedDate = plannedDate;
     * }
     *
     */
}
