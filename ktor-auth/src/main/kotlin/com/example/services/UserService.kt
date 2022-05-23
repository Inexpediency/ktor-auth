package com.example.services

import com.example.dao.JwtUserDao
import com.example.dto.JwtUserDto
import com.example.dto.JwtUserPublicDto
import com.example.dto.UserIdDto

interface UserService {
    suspend fun create(jwtUserDto: JwtUserDto): UserIdDto?
    suspend fun get(id: Int): JwtUserPublicDto?
}
