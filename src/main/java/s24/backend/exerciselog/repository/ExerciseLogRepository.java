package s24.backend.exerciselog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import s24.backend.exerciselog.domain.*;

import java.util.List;


public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {
    List<ExerciseLog> findByUser(User user); // all by user
    List<ExerciseLog> findByUserOrderByDate(User user); // all by user sort by completion date
    List<ExerciseLog> findByUserAndNameIgnoreCase(User user, String name); // all of user's specific exercise
    List<ExerciseLog> findByUserAndNameIgnoreCaseOrderByDate(User user, String name); // all of user's specific exercise sort by completion date

    // Need this for handling deletion correctly to set each of these objects null
    List<ExerciseLog> findByWorkout(Workout workout);
    List<ExerciseLog> findByPlannedExerciseLog(PlannedExerciseLog plannedExerciseLog);
}
