package s24.backend.exerciselog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import s24.backend.exerciselog.domain.ExerciseLog;
import s24.backend.exerciselog.domain.User;

import java.util.List;


public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {
    List<ExerciseLog> findByUser(User user); // all by user
    List<ExerciseLog> findByUserOrderByCompletedWorkout_DateDesc(User user); // all by user sort by completion date
    List<ExerciseLog> findByUserAndNameIgnoreCase(User user, String name); // all of user's specific exercise
    List<ExerciseLog> findByUserAndNameIgnoreCaseOrderByCompletedWorkout_DateDesc(User user, String name); // all of user's specific exercise sort by completion date
}
