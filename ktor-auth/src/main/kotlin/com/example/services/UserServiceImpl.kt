package com.example.services

import com.example.dao.JwtUserDao
import com.example.dto.JwtUserDto
import com.example.dto.JwtUserPublicDto
import com.example.dto.UserIdDto
import org.mindrot.jbcrypt.BCrypt

class UserServiceImpl(private val jwtUserDao: JwtUserDao) : UserService {
    override suspend fun create(jwtUserDto: JwtUserDto): UserIdDto? = jwtUserDao.create(
        jwtUserDto.username,
        BCrypt.hashpw(jwtUserDto.password, BCrypt.gensalt())
    )?.let {
        UserIdDto(it.id)
    }

    override suspend fun get(id: Int): JwtUserPublicDto? = jwtUserDao.getById(id)?.let {
        JwtUserPublicDto(it.username)
    }
}
