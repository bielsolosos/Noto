package space.bielsolososdev.noto.api.model.user

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ChangePasswordRequest(
    @field:NotBlank
    @field:Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    val oldPassword: String,

    @field:NotBlank
    @field:Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    val newPassword: String
)

