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
        exception<Throwable> { call, cause ->
            val statusCode: HttpStatusCode = when (cause) {
                is AuthenticationException -> HttpStatusCode.Unauthorized
                is AuthorizationException -> HttpStatusCode.Forbidden
                is BadRequestException -> HttpStatusCode.BadRequest
                is ContentTransformationException -> HttpStatusCode.BadRequest
                else -> HttpStatusCode.InternalServerError
            }

            call.respond(statusCode, ErrorDto(cause.message ?: "", statusCode.value))
        }
    }
}

class AuthenticationException(message: String = "") : RuntimeException(message)
class AuthorizationException(message: String = "") : RuntimeException(message)

fun <T : Any> T.validate(validator: Validator) {
    validator.validate(this)
        .takeIf { it.isNotEmpty() }
        ?.let { throw BadRequestException(it.first().messageWithFieldName()) }
}

fun <T : Any> ConstraintViolation<T>.messageWithFieldName() = "${this.propertyPath} ${this.message}"
