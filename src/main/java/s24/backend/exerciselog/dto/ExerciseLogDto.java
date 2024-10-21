package s24.backend.exerciselog.dto;

import java.util.List;

import jakarta.validation.Valid;

import java.time.LocalDate;

/*
 * Long exerciseId
 * List<SetLogDto> setData 
 *      - int setNumber
 *        int reps
 *        double weight
 * String exerciseNotes
 * String exerciseName
 * LocalDate date => date of completion
 */

public class ExerciseLogDto {
    
    private Long exerciseId;
    @Valid
    private List<SetLogDto> setData;
    private String exerciseNotes;
    private String exerciseName;
    private LocalDate date;
    public ExerciseLogDto() {
    }
    public List<SetLogDto> getSetData() {
        return setData;
    }
    public void setSetData(List<SetLogDto> setData) {
        this.setData = setData;
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
