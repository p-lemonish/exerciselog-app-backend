package s24.backend.exerciselog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import s24.backend.exerciselog.domain.Workout;

public interface WorkoutRepository extends JpaRepository<Workout, Long>{

}
