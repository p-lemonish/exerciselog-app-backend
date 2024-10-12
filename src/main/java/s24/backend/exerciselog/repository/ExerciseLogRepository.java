package s24.backend.exerciselog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import s24.backend.exerciselog.domain.ExerciseLog;
import s24.backend.exerciselog.domain.User;

import java.util.List;


public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {
    List<ExerciseLog> findByUser(User user);
    List<ExerciseLog> findByUserOrderByNameAsc(User user);
    List<ExerciseLog> findByUserAndName(User user, String name);
}
