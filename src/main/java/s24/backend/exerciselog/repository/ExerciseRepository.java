package s24.backend.exerciselog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import s24.backend.exerciselog.domain.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

}
