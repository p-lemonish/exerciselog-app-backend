package s24.backend.exerciselog.domain;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Workout> workouts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CompletedWorkout> completedWorkouts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PlannedExerciseLog> plannedExerciseLogs;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Exercise> exercises;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private String username;
    private String password;
    private String email;

    public User(List<Workout> workouts, List<CompletedWorkout> completedWorkouts,
            List<PlannedExerciseLog> plannedExerciseLogs, Role role, String username, String password, String email) {
        this.workouts = workouts;
        this.completedWorkouts = completedWorkouts;
        this.plannedExerciseLogs = plannedExerciseLogs;
        this.role = role;
        this.username = username;
        this.password = password;
        this.email = email;
    }
    public User() {
    }
    public List<PlannedExerciseLog> getPlannedExerciseLogs() {
        return plannedExerciseLogs;
    }
    public void setPlannedExerciseLogs(List<PlannedExerciseLog> plannedExerciseLogs) {
        this.plannedExerciseLogs = plannedExerciseLogs;
    }
    public List<CompletedWorkout> getCompletedWorkouts() {
        return completedWorkouts;
    }
    public void setCompletedWorkouts(List<CompletedWorkout> completedWorkouts) {
        this.completedWorkouts = completedWorkouts;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    public List<Workout> getWorkouts() {
        return workouts;
    }
    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
