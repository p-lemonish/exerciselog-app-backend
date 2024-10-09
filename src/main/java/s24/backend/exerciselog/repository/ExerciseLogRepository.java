package s24.backend.exerciselog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import s24.backend.exerciselog.domain.ExerciseLog;

public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {
    
}
