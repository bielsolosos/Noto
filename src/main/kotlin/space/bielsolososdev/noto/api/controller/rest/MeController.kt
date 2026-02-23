package space.bielsolososdev.noto.api.controller.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import space.bielsolososdev.noto.api.mapper.UserMapper
import space.bielsolososdev.noto.api.model.user.UserResponse
import space.bielsolososdev.noto.domain.users.service.UserService

@RestController
@RequestMapping("/api/me")
class MeController(
    private val userService: UserService
) {

    @GetMapping
    fun getMe(): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(UserMapper.toUserResponse(userService.getMe()))
    }
}

