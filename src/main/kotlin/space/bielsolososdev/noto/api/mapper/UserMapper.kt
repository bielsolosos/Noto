package space.bielsolososdev.noto.api.mapper

import space.bielsolososdev.noto.api.model.user.UserResponse
import space.bielsolososdev.noto.domain.users.model.User

object UserMapper {

    fun toUserResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id,
            username = user.username,
            email = user.email,
            isActive = user.isActive,
            createdAt = user.createdAt,
            roles = user.roles.map { it.name }.toSet()
        )
    }
}

