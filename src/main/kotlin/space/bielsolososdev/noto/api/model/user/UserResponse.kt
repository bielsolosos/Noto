package space.bielsolososdev.noto.api.model.user

import java.time.LocalDateTime

data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val isActive: Boolean,
    val createdAt: LocalDateTime?,
    val roles: Set<String>
)

