package s24.backend.exerciselog.domain.entity;

import java.util.List;

import jakarta.persistence.*;

/*
 * TODO Make exercises unique to users only, currently any user can use any exerciseId that exists in the database
 */

@Entity
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "exercise")
    private List<ExerciseLog> exerciseLogs;

    @OneToMany(mappedBy = "exercise")
    private List<PlannedExerciseLog> plannedExerciseLogs;
    
    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private User user;

    private String name;
    private String muscleGroup;

    public Exercise() {}
    public Exercise(String name, String muscleGroup) {
        this.name = name;
        this.muscleGroup = muscleGroup;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public List<PlannedExerciseLog> getPlannedExerciseLogs() {
        return plannedExerciseLogs;
    }
    public void setPlannedExerciseLogs(List<PlannedExerciseLog> plannedExerciseLogs) {
        this.plannedExerciseLogs = plannedExerciseLogs;
    }
    public List<ExerciseLog> getExerciseLogs() {
        return exerciseLogs;
    }
    public void setExerciseLogs(List<ExerciseLog> exerciseLogs) {
        this.exerciseLogs = exerciseLogs;
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
    public String getMuscleGroup() {
        return muscleGroup;
    }
    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }
}
