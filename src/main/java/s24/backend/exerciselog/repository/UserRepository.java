package s24.backend.exerciselog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import s24.backend.exerciselog.domain.User;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
}
