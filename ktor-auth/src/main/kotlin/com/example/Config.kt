package com.example

import io.ktor.server.config.*

data class StorageConfig(val driverClassName: String, val jdbcURL: String) {
    constructor(config: ApplicationConfig) : this(
        config.property("storage.driverClassName").getString(),
        config.property("storage.jdbcURL").getString()
    )
}

data class JwtConfig(val secretKey: String, val ttl: Int, val headerName: String) {
    constructor(config: ApplicationConfig) : this(
        config.property("jwt.secretKey").getString(),
        config.property("jwt.ttl").getString().toInt(),
        config.property("jwt.headerName").getString(),
    )
}
