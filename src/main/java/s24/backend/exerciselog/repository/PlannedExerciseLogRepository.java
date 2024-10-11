package s24.backend.exerciselog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import s24.backend.exerciselog.domain.PlannedExerciseLog;

public interface PlannedExerciseLogRepository extends JpaRepository<PlannedExerciseLog, Long> {
    
}
