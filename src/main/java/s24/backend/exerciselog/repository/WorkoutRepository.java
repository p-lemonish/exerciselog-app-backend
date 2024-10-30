package s24.backend.exerciselog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import s24.backend.exerciselog.domain.entity.PlannedExerciseLog;
import s24.backend.exerciselog.domain.entity.User;
import s24.backend.exerciselog.domain.entity.Workout;

public interface WorkoutRepository extends JpaRepository<Workout, Long>{
    List<Workout> findByUser(User user);
    List<Workout> findByPlannedExerciseLogs(PlannedExerciseLog plannedExerciseLog);
}
