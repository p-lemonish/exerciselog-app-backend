package s24.backend.exerciselog.domain.dto;

import java.util.List;

import jakarta.validation.Valid;

import java.time.LocalDate;

/*
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
    // TODO bring exerciseLog id as well
    private Long exerciseId;
    @Valid
    private List<SetLogDto> setLogDtoList;
    private String exerciseNotes;
    private String exerciseName;
    private LocalDate date;
    public ExerciseLogDto() {
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
