package space.bielsolososdev.noto.domain.users.model.dto

data class TokenResponse(
    val token: String,
    val refreshToken: String
)

