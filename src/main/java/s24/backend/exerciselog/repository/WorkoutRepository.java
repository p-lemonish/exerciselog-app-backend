package s24.backend.exerciselog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import s24.backend.exerciselog.domain.PlannedExerciseLog;
import s24.backend.exerciselog.domain.User;
import s24.backend.exerciselog.domain.Workout;

public interface WorkoutRepository extends JpaRepository<Workout, Long>{
    List<Workout> findByUser(User user);
    List<Workout> findByPlannedExerciseLogs(PlannedExerciseLog plannedExerciseLog);
}
