package s24.backend.exerciselog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import s24.backend.exerciselog.domain.PlannedExercise;

public interface PlannedExerciseRepository extends JpaRepository<PlannedExercise, Long> {
    
}
