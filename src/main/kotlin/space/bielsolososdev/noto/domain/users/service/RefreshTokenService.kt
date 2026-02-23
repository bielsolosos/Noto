package space.bielsolososdev.noto.domain.users.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import space.bielsolososdev.noto.infrastructure.NotoProperties
import java.time.Instant
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class RefreshTokenService(
    private val properties: NotoProperties
) {

    private val log = LoggerFactory.getLogger(RefreshTokenService::class.java)
    private val tokens = ConcurrentHashMap<String, RefreshTokenData>()

    fun createRefreshToken(username: String): String {
        val token = UUID.randomUUID().toString()
        val expiresAt = Instant.now().plusMillis(properties.jwt.refreshExpiration)
        tokens[token] = RefreshTokenData(username, expiresAt)
        return token
    }

    fun validateAndConsume(token: String): String {
        val data = tokens.remove(token)
            ?: throw IllegalArgumentException("Refresh token inválido ou já utilizado")

        if (data.expiresAt.isBefore(Instant.now())) {
            throw IllegalArgumentException("Refresh token expirado")
        }

        return data.username
    }

    fun cleanupExpiredTokens() {
        val now = Instant.now()
        val initialSize = tokens.size
        tokens.entries.removeIf { it.value.expiresAt.isBefore(now) }
        val removed = initialSize - tokens.size
        if (removed > 0) {
            log.debug("Removed {} expired refresh tokens", removed)
        }
    }

    private data class RefreshTokenData(val username: String, val expiresAt: Instant)
}

