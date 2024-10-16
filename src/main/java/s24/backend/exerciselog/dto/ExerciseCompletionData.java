package s24.backend.exerciselog.dto;

import java.util.List;
import java.time.LocalDate;

public class ExerciseCompletionData {
    
    private Long exerciseId;
    private List<SetData> setData;
    private String exerciseNotes;
    private String exerciseName;
    private LocalDate date;
    public ExerciseCompletionData() {
    }
    public List<SetData> getSetData() {
        return setData;
    }
    public void setSetData(List<SetData> setData) {
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
