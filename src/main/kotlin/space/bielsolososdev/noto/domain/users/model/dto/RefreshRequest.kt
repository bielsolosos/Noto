package space.bielsolososdev.noto.domain.users.model.dto

import jakarta.validation.constraints.NotBlank

data class RefreshRequest(
    @field:NotBlank(message = "É obrigatório ter um refresh token")
    val refreshToken: String
)

