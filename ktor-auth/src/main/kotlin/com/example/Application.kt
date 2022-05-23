package com.example

import com.example.dao.DatabaseFactory
import com.example.plugins.configureExceptionHandler
import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    DatabaseFactory.init(StorageConfig(environment.config))
    configureSerialization()
    configureExceptionHandler()
    configureRouting(JwtConfig(environment.config))
}
