package space.bielsolososdev.noto.api.controller.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import space.bielsolososdev.noto.domain.users.model.dto.LoginRequest
import space.bielsolososdev.noto.domain.users.model.dto.RefreshRequest
import space.bielsolososdev.noto.domain.users.model.dto.TokenResponse
import space.bielsolososdev.noto.domain.users.service.AuthService

@RestController
@RequestMapping("/api/auth")
class AuthRestController(
    private val service: AuthService
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<TokenResponse> {
        return ResponseEntity.ok(service.login(request))
    }

    @PostMapping("/refresh")
    fun refresh(@RequestBody request: RefreshRequest): ResponseEntity<TokenResponse> {
        return ResponseEntity.ok(service.refresh(request))
    }
}

