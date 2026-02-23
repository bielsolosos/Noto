package space.bielsolososdev.noto.domain.users.model.dto

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "Username é obrigatório")
    val username: String,

    @field:NotBlank(message = "Password é obrigatório")
    val password: String
)

