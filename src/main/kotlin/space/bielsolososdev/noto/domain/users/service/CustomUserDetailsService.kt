package space.bielsolososdev.noto.domain.users.service

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import space.bielsolososdev.noto.domain.users.repository.UserRepository

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("Usuário não encontrado") }

        return org.springframework.security.core.userdetails.User
            .withUsername(user.username)
            .password(user.password)
            .authorities(user.roles.map { role -> SimpleGrantedAuthority(role.name) })
            .accountExpired(!user.isActive)
            .accountLocked(!user.isActive)
            .disabled(!user.isActive)
            .build()
    }
}

