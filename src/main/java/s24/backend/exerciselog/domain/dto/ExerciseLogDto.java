package s24.backend.exerciselog.domain.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

/*
 * Long id => ExerciseLog id
 * Long exerciseId
 * List<SetLogDto> setLogDtoList 
 *      - int setNumber
 *        int reps
 *        double weight
 * String exerciseNotes
 * String exerciseName
 * LocalDate date => date of completion
 */

public class ExerciseLogDto {
    private Long id;
    private Long exerciseId;
    @Valid
    private List<SetLogDto> setLogDtoList;
    private String exerciseNotes;
    @NotBlank(message = "Exercise name must be present")
    private String exerciseName;
    private LocalDate date;
    public ExerciseLogDto() {}
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public List<SetLogDto> getSetLogDtoList() {
        return setLogDtoList;
    }
    public void setSetLogDtoList(List<SetLogDto> setLogDtoList) {
        this.setLogDtoList = setLogDtoList;
    }
    public String getExerciseName() {
        return exerciseName;
    }
    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }
    public Long getExerciseId() {
        return exerciseId;
    }
    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }
    public String getExerciseNotes() {
        return exerciseNotes;
    }
    public void setExerciseNotes(String exerciseNotes) {
        this.exerciseNotes = exerciseNotes;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
}
