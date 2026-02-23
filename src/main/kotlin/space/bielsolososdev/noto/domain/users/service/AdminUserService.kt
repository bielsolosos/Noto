package space.bielsolososdev.noto.domain.users.service

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import space.bielsolososdev.noto.core.exception.BusinessException
import space.bielsolososdev.noto.domain.users.model.User
import space.bielsolososdev.noto.domain.users.repository.UserRepository
import space.bielsolososdev.noto.domain.users.repository.specification.UserSpecification
import java.time.LocalDateTime

@Service
class AdminUserService(
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userService: UserService
) {

    private val log = LoggerFactory.getLogger(AdminUserService::class.java)

    fun listUsers(
        pageable: Pageable,
        filter: String?,
        isActive: Boolean?,
        createdAfter: LocalDateTime?,
        createdBefore: LocalDateTime?
    ): Page<User> {
        val spec = UserSpecification(filter, isActive, createdAfter, createdBefore)
        return repository.findAll(spec, pageable)
    }

    fun toggleUserActive(id: Long): User {
        log.debug("Alternando status ativo do usuário com ID: {}", id)

        val user = userService.getEntity(id)
        user.isActive = !user.isActive

        val savedUser = repository.save(user)
        log.info(
            "Status do usuário {} alterado para: {}",
            user.username,
            if (user.isActive) "ATIVO" else "INATIVO"
        )

        return savedUser
    }

    fun deleteUser(id: Long) {
        log.debug("Tentativa de deletar usuário com ID: {}", id)

        val user = userService.getEntity(id)
        repository.delete(user)

        log.warn("Usuário {} (ID: {}) foi DELETADO do sistema", user.username, id)
    }

    fun adminChangePassword(id: Long, newPassword: String): User {
        log.debug("Admin alterando senha do usuário com ID: {}", id)

        val user = userService.getEntity(id)
        user.password = passwordEncoder.encode(newPassword)

        val savedUser = repository.save(user)
        log.info("Senha do usuário {} alterada por administrador", user.username)

        return savedUser
    }

    fun adminEditUser(id: Long, username: String, email: String): User {
        val entity = userService.getEntity(id)

        if (entity.username != username) {
            repository.findByUsername(username).ifPresent {
                throw BusinessException("Nome de usuário já existente")
            }
        }

        if (entity.email != email) {
            repository.findByEmail(email).ifPresent {
                throw BusinessException("Email já existente")
            }
        }

        entity.username = username
        entity.email = email

        val savedUser = repository.save(entity)
        log.info("Admin atualizou usuário {}: username='{}', email='{}'", id, username, email)

        return savedUser
    }
}

