package s24.backend.exerciselog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import s24.backend.exerciselog.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    
}
