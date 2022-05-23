package com.example.plugins

import com.example.dto.ErrorDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import javax.validation.ConstraintViolation
import javax.validation.Validator

fun Application.configureExceptionHandler() {
    install(StatusPages) {
        exception<AuthenticationException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<AuthorizationException> { call, cause ->
            call.respond(HttpStatusCode.Forbidden)
        }
        exception<BadRequestException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorDto(cause.message ?: "", HttpStatusCode.BadRequest.value)
            )
        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()

@Throws(BadRequestException::class)
fun <T : Any> T.validate(validator: Validator) {
    validator.validate(this)
        .takeIf { it.isNotEmpty() }
        ?.let { throw BadRequestException(it.first().messageWithFieldName()) }
}

fun <T : Any> ConstraintViolation<T>.messageWithFieldName() = "${this.propertyPath} ${this.message}"
