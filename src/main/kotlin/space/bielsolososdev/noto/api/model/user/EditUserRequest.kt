package space.bielsolososdev.noto.api.model.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class EditUserRequest(
    @field:NotBlank
    val username: String,

    @field:NotBlank
    @field:Email(message = "Deve ser um email válido")
    val email: String
)

