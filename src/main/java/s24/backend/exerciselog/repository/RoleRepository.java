package s24.backend.exerciselog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import s24.backend.exerciselog.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
