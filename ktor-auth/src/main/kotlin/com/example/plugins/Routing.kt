package com.example.plugins

import com.example.dao.JwtUserDaoImpl
import com.example.dto.JwtUserDto
import com.example.services.JwtAuthService
import com.example.services.UserServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator

fun Application.configureRouting() {
    val validator = Validation.buildDefaultValidatorFactory().validator

    val jwtUserDao = JwtUserDaoImpl()
    val userService = UserServiceImpl(jwtUserDao)
    val jwtAuthService = JwtAuthService(jwtUserDao)

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        post("/create_user") {
            val jwtUserDto = call.receive<JwtUserDto>()
            jwtUserDto.validate(validator)

            val result = userService.create(jwtUserDto)

            if (result != null) {
                call.respond(result)
            } else {
                call.respond(HttpStatusCode.BadRequest, "user already exists")
            }
        }

        post("/sign_in") {
            val jwtUserDto = call.receive<JwtUserDto>()
            jwtUserDto.validate(validator)

            val result = jwtAuthService.signIn(jwtUserDto)

            if (result != null) {
                call.respond(result)
            } else {
                call.respond(HttpStatusCode.Unauthorized, "invalid username or password")
            }
        }

        get("/whoami") {
            val token = call.request.header("Token")
            if (token == null || token == "") {
                call.respond(HttpStatusCode.Unauthorized, "empty request header")

                return@get
            }

            val result = jwtAuthService.whoami(token)

            if (result != null) {
                call.respond(result)
            } else {
                call.respond(HttpStatusCode.Unauthorized, "invalid token")
            }
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
