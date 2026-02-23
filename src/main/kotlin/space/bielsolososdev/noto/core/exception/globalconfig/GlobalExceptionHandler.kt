package space.bielsolososdev.noto.core.exception.globalconfig

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.resource.NoResourceFoundException
import space.bielsolososdev.noto.core.exception.BusinessException
import jakarta.servlet.http.HttpServletRequest

data class MessageResponse(val message: String)

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException, request: WebRequest): ResponseEntity<MessageResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResponse(ex.message ?: "Erro de negócio"))
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentials(ex: BadCredentialsException, request: WebRequest): ResponseEntity<MessageResponse> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(MessageResponse("Usuário ou senha incorretos"))
    }

    @ExceptionHandler(DisabledException::class)
    fun handleDisabled(ex: DisabledException, request: WebRequest): ResponseEntity<MessageResponse> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(MessageResponse("Conta desabilitada"))
    }

    @ExceptionHandler(LockedException::class)
    fun handleLocked(ex: LockedException, request: WebRequest): ResponseEntity<MessageResponse> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(MessageResponse("Conta bloqueada"))
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(ex: AuthenticationException, request: WebRequest): ResponseEntity<MessageResponse> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(MessageResponse("Falha na autenticação"))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException, request: WebRequest): ResponseEntity<MessageResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResponse(ex.message ?: "Argumento inválido"))
    }

    @ExceptionHandler(AuthorizationDeniedException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleAuthorizationDenied(ex: AuthorizationDeniedException, request: HttpServletRequest): MessageResponse {
        log.warn("Acesso negado para {}: {}", request.requestURI, ex.message)
        return MessageResponse("Acesso negado: você não tem permissão para acessar este recurso")
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<MessageResponse> {
        val errorMessage = ex.bindingResult.fieldErrors
            .firstNotNullOfOrNull { it.defaultMessage }
            ?: "Dados inválidos"
        return ResponseEntity.badRequest().body(MessageResponse(errorMessage))
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNotFound(ex: NoHandlerFoundException, request: WebRequest): ResponseEntity<MessageResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(MessageResponse("Rota não encontrada: ${ex.requestURL}"))
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResource(ex: NoResourceFoundException, request: HttpServletRequest): ResponseEntity<MessageResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(MessageResponse("Rota não encontrada: ${request.requestURI}"))
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(ex: Exception, request: WebRequest): ResponseEntity<MessageResponse> {
        log.error("Erro inesperado: ", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(MessageResponse("Ocorreu um erro inesperado no servidor"))
    }
}

