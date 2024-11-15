package s24.backend.exerciselog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import s24.backend.exerciselog.domain.entity.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameHash(String usernameHash);
    Optional<User> findByEmailHash(String emailHash);
    Optional<User> findById(Long id);
}
