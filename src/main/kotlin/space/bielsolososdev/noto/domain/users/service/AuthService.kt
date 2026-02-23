package space.bielsolososdev.noto.domain.users.service

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Service
import space.bielsolososdev.noto.core.utils.SecurityUtils
import space.bielsolososdev.noto.domain.users.model.dto.LoginRequest
import space.bielsolososdev.noto.domain.users.model.dto.RefreshRequest
import space.bielsolososdev.noto.domain.users.model.dto.TokenResponse

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: SecurityUtils,
    private val refreshTokenService: RefreshTokenService
) {

    @Throws(AuthenticationException::class)
    fun login(request: LoginRequest): TokenResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )

        val token = jwtUtil.generateToken(request.username)
        val refreshToken = refreshTokenService.createRefreshToken(request.username)

        return TokenResponse(token, refreshToken)
    }

    fun refresh(request: RefreshRequest): TokenResponse {
        val username = refreshTokenService.validateAndConsume(request.refreshToken)
        return TokenResponse(
            jwtUtil.generateToken(username),
            refreshTokenService.createRefreshToken(username)
        )
    }
}

