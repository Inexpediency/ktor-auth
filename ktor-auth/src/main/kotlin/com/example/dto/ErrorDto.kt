package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(val message: String, val errorCode: Int)
