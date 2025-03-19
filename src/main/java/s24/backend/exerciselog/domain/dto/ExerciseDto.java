package s24.backend.exerciselog.domain.dto;

import jakarta.validation.constraints.NotEmpty;

/*
 * Long exerciseId
 * Long userId
 * String name
 */

public class ExerciseDto {
    private Long exerciseId;
    private Long userId;

    @NotEmpty(message = "Exercise must have a name")
    private String name;

    public ExerciseDto() {
    }

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
}
