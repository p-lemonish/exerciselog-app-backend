package s24.backend.exerciselog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import s24.backend.exerciselog.domain.Exercise;


public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Optional<Exercise> findByName(String name);
}
