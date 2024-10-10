package s24.backend.exerciselog.domain;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "exercise")
    private List<ExerciseLog> exerciseLogs;

    @OneToMany(mappedBy = "exercise")
    private List<PlannedExercise> plannedExercises;

    private String name;
    private String muscleGroup;

    public Exercise() {
    }
    public Exercise(String name, String muscleGroup) {
        this.name = name;
        this.muscleGroup = muscleGroup;
    }
    public List<PlannedExercise> getPlannedExercises() {
        return plannedExercises;
    }
    public void setPlannedExercises(List<PlannedExercise> plannedExercises) {
        this.plannedExercises = plannedExercises;
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
