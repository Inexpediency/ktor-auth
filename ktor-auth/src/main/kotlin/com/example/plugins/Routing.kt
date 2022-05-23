package com.example.plugins

import com.example.JwtConfig
import com.example.dao.JwtUserDaoImpl
import com.example.dto.JwtUserDto
import com.example.services.JwtAuthService
import com.example.services.UserServiceImpl
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import javax.validation.Validation

fun Application.configureRouting(jwtConfig: JwtConfig) {
    val validator = Validation.buildDefaultValidatorFactory().validator

    val jwtUserDao = JwtUserDaoImpl()
    val userService = UserServiceImpl(jwtUserDao)
    val jwtAuthService = JwtAuthService(jwtUserDao, jwtConfig)

    routing {
        post("/create_user") {
            val jwtUserDto = call.receive<JwtUserDto>()
            jwtUserDto.validate(validator)

            call.respond(userService.create(jwtUserDto))
        }

        post("/sign_in") {
            val jwtUserDto = call.receive<JwtUserDto>()
            jwtUserDto.validate(validator)

            val result = jwtAuthService.signIn(jwtUserDto)
                ?: throw AuthenticationException("invalid username or password")

            call.respond(result)
        }

        get("/whoami") {
            val token = call.request.header(jwtConfig.headerName)
            if (token == null || token == "") {
                throw AuthenticationException("empty request header")
            }

            val result = jwtAuthService.whoami(token) ?: throw AuthenticationException("invalid token")

            call.respond(result)
        }
    }
}
