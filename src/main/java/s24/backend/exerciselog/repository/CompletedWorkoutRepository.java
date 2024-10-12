package s24.backend.exerciselog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import s24.backend.exerciselog.domain.CompletedWorkout;

public interface CompletedWorkoutRepository extends JpaRepository<CompletedWorkout, Long>{

}
