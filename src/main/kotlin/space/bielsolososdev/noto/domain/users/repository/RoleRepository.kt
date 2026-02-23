package space.bielsolososdev.noto.domain.users.repository

import org.springframework.data.jpa.repository.JpaRepository
import space.bielsolososdev.noto.domain.users.model.Role
import java.util.Optional

interface RoleRepository : JpaRepository<Role, Long> {

    fun findByName(name: String): Optional<Role>
}

