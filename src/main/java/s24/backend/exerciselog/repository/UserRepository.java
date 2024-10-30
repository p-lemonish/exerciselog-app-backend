package s24.backend.exerciselog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import s24.backend.exerciselog.domain.entity.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
}
