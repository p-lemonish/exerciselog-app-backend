package s24.backend.exerciselog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import s24.backend.exerciselog.domain.entity.CompletedWorkout;
import s24.backend.exerciselog.domain.entity.User;

import java.util.List;


public interface CompletedWorkoutRepository extends JpaRepository<CompletedWorkout, Long>{
    List<CompletedWorkout> findByUser(User user);
}
