package space.bielsolososdev.noto.api.model.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateUserRequest(
    @field:NotBlank
    val username: String,

    @field:NotBlank
    @field:Email(message = "Deve ser um email válido")
    val email: String,

    @field:NotBlank
    @field:Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    val password: String,

    @field:NotBlank
    @field:Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    val confirmPassword: String
)

