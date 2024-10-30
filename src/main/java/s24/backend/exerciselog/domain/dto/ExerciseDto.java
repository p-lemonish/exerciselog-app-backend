package s24.backend.exerciselog.domain.dto;

import jakarta.validation.constraints.NotEmpty;

/*
 * Long exerciseId
 * Long userId
 * String name
 * String muscleGroup
 */

public class ExerciseDto {
    private Long exerciseId;
    private Long userId;

    @NotEmpty(message = "Exercise must have a name")
    private String name;

    @NotEmpty(message = "Exercise must have a target muscle group")
    private String muscleGroup;

    public ExerciseDto() {}
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
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
