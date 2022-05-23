package com.example.dto

import org.hibernate.validator.constraints.Length
import kotlinx.serialization.Serializable

@Serializable
data class JwtUserDto(
    @field:Length(min=5, max=15)
    val username: String,

    @field:Length(min=5, max=15)
    val password: String,
)
