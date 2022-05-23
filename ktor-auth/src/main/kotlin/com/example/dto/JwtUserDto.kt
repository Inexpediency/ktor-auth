package com.example.dto

import kotlinx.serialization.Serializable
import org.hibernate.validator.constraints.Length

@Serializable
data class JwtUserDto(
    @field:Length(min = 5, max = 15)
    val username: String,

    @field:Length(min = 5, max = 15)
    val password: String,
)
