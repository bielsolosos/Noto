package space.bielsolososdev.noto.domain.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import space.bielsolososdev.noto.domain.users.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}

