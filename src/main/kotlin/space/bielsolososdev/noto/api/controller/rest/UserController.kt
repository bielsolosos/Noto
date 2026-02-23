package space.bielsolososdev.noto.api.controller.rest

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.NoHandlerFoundException
import space.bielsolososdev.noto.api.mapper.UserMapper
import space.bielsolososdev.noto.api.model.MessageResponse
import space.bielsolososdev.noto.api.model.user.ChangePasswordRequest
import space.bielsolososdev.noto.api.model.user.CreateUserRequest
import space.bielsolososdev.noto.api.model.user.EditUserRequest
import space.bielsolososdev.noto.api.model.user.UserResponse
import space.bielsolososdev.noto.core.exception.BusinessException
import space.bielsolososdev.noto.domain.users.service.UserService
import space.bielsolososdev.noto.infrastructure.NotoProperties

@RestController
@RequestMapping("/api/users")
class UserController(
    private val service: UserService,
    private val props: NotoProperties
) {

    @PatchMapping("/change-password")
    fun changePassword(@Valid @RequestBody request: ChangePasswordRequest): ResponseEntity<MessageResponse> {
        service.changePassword(service.getMe().id, request.oldPassword, request.newPassword)
        return ResponseEntity.ok(MessageResponse("Senha alterada com sucesso"))
    }

    @PatchMapping("/edit-credentials")
    fun editUser(@Valid @RequestBody request: EditUserRequest): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(
            UserMapper.toUserResponse(
                service.editUser(service.getMe().id, request.username, request.email)
            )
        )
    }

    @PostMapping("/register")
    fun createUser(@Valid @RequestBody request: CreateUserRequest): ResponseEntity<UserResponse> {
        if (!props.registrationEnabled) {
            throw NoHandlerFoundException(
                "POST",
                "/api/users/register",
                org.springframework.http.HttpHeaders()
            )
        }

        if (request.password != request.confirmPassword) {
            throw BusinessException("As senhas não conferem")
        }

        val userResponse = UserMapper.toUserResponse(
            service.createUser(request.username, request.email, request.password)
        )

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse)
    }
}

