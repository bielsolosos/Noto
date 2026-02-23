package space.bielsolososdev.noto.domain.users.service

import org.slf4j.LoggerFactory
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import space.bielsolososdev.noto.core.exception.BusinessException
import space.bielsolososdev.noto.domain.users.model.User
import space.bielsolososdev.noto.domain.users.repository.RoleRepository
import space.bielsolososdev.noto.domain.users.repository.UserRepository

@Service
class UserService(
    private val repository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder
) {

    private val log = LoggerFactory.getLogger(UserService::class.java)

    fun getMe(): User {
        log.debug("Buscando informações do usuário autenticado")
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication == null || !authentication.isAuthenticated) {
            log.warn("Tentativa de acesso sem autenticação válida")
            throw BusinessException("Usuário não autenticado")
        }

        val username = authentication.name
        log.info("Usuário '{}' acessando seu próprio perfil", username)

        return repository.findByUsername(username).orElseThrow {
            log.error("Usuário autenticado '{}' não encontrado no banco de dados", username)
            BusinessException("Usuário não encontrado")
        }
    }

    fun changePassword(id: Long, oldPassword: String, newPassword: String): User {
        log.debug("Tentativa de alteração de senha para o usuário com ID: {}", id)

        val user = repository.findById(id).orElseThrow {
            log.error("Usuário com ID {} não encontrado ao tentar alterar senha", id)
            BusinessException("User não encontrado")
        }

        if (!passwordEncoder.matches(oldPassword, user.password)) {
            log.warn("Tentativa de alteração de senha com senha atual incorreta para usuário: {}", user.username)
            throw BusinessException("Senha atual incorreta")
        }

        user.password = passwordEncoder.encode(newPassword)
        val savedUser = repository.save(user)

        log.info("Senha alterada com sucesso para o usuário: {}", user.username)
        return savedUser
    }

    fun createUser(username: String, email: String, password: String): User {
        log.debug("Tentativa de criação de novo usuário: username='{}', email='{}'", username, email)

        if (repository.findByUsername(username).isPresent) {
            log.warn("Tentativa de criar usuário com username já existente: {}", username)
            throw BusinessException("Nome de usuário já está em uso")
        }

        if (repository.findByEmail(email).isPresent) {
            log.warn("Tentativa de criar usuário com email já existente: {}", email)
            throw BusinessException("E-mail já está em uso")
        }

        val entity = User().apply {
            this.email = email
            this.username = username
            this.password = passwordEncoder.encode(password)
            this.roles.add(roleRepository.findByName("ROLE_USER").get())
        }

        val savedUser = repository.save(entity)
        log.info("✅ Novo usuário criado com sucesso: {} (ID: {})", username, savedUser.id)

        return savedUser
    }

    fun editUser(id: Long, username: String, email: String): User {
        val entity = getEntity(id)

        verifyUserIntegrity(entity)

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
        log.info("Usuário {} atualizado com sucesso: username='{}', email='{}'", id, username, email)

        return savedUser
    }

    fun getEntity(id: Long): User {
        return repository.findById(id)
            .orElseThrow { BusinessException("Usuário não encontrado no sistema.") }
    }

    private fun verifyUserIntegrity(entity: User) {
        if (entity.id != getMe().id) {
            throw BadCredentialsException("Sem permissão.")
        }
    }
}

