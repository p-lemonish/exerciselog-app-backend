package s24.backend.exerciselog.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import s24.backend.exerciselog.domain.*;


public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Optional<Exercise> findByName(String name);
    List<Exercise> findByUser(User user);
}
