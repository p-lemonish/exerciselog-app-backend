package s24.backend.exerciselog.dto;

import java.util.List;

public class WorkoutCompletionForm {
    
    private String workoutNotes;
    private List<ExerciseCompletionData> exercises;
    public WorkoutCompletionForm() {
    }
    public String getWorkoutNotes() {
        return workoutNotes;
    }
    public void setWorkoutNotes(String workoutNotes) {
        this.workoutNotes = workoutNotes;
    }
    public List<ExerciseCompletionData> getExercises() {
        return exercises;
    }
    public void setExercises(List<ExerciseCompletionData> exercises) {
        this.exercises = exercises;
    }
}
