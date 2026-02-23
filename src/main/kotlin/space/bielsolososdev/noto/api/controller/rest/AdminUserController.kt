package space.bielsolososdev.noto.api.controller.rest

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import space.bielsolososdev.noto.api.annotations.IsAdmin
import space.bielsolososdev.noto.api.mapper.UserMapper
import space.bielsolososdev.noto.api.model.MessageResponse
import space.bielsolososdev.noto.api.model.user.ChangePasswordRequest
import space.bielsolososdev.noto.api.model.user.EditUserRequest
import space.bielsolososdev.noto.api.model.user.UserResponse
import space.bielsolososdev.noto.domain.users.service.AdminUserService
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/admin/users")
@IsAdmin
class AdminUserController(
    private val userService: AdminUserService
) {

    @GetMapping
    fun listUsers(
        @PageableDefault(size = 20, sort = ["createdAt"]) pageable: Pageable,
        @RequestParam(required = false) filter: String?,
        @RequestParam(required = false) isActive: Boolean?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) createdAfter: LocalDateTime?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) createdBefore: LocalDateTime?
    ): ResponseEntity<Page<UserResponse>> {
        val response = userService.listUsers(pageable, filter, isActive, createdAfter, createdBefore)
            .map { UserMapper.toUserResponse(it) }
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/{id}/credentials")
    fun editUser(
        @PathVariable id: Long,
        @Valid @RequestBody request: EditUserRequest
    ): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(
            UserMapper.toUserResponse(userService.adminEditUser(id, request.username, request.email))
        )
    }

    @PatchMapping("/{id}/password")
    fun changePassword(
        @PathVariable id: Long,
        @Valid @RequestBody request: ChangePasswordRequest
    ): ResponseEntity<MessageResponse> {
        userService.adminChangePassword(id, request.newPassword)
        return ResponseEntity.ok(MessageResponse("Senha alterada com sucesso"))
    }

    @PatchMapping("/{id}/toggle-active")
    fun toggleActive(@PathVariable id: Long): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(UserMapper.toUserResponse(userService.toggleUserActive(id)))
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<MessageResponse> {
        userService.deleteUser(id)
        return ResponseEntity.ok(MessageResponse("Usuário deletado com sucesso"))
    }
}

