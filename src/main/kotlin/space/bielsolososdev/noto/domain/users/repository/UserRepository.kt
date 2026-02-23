package space.bielsolososdev.noto.domain.users.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import space.bielsolososdev.noto.domain.users.model.User
import java.util.Optional

interface UserRepository : JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    fun findByUsername(username: String): Optional<User>

    fun findByEmail(email: String): Optional<User>
}

