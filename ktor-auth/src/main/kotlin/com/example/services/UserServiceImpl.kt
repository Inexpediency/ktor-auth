package com.example.services

import com.example.dao.JwtUserDao
import com.example.dto.JwtUserDto
import com.example.dto.JwtUserPublicDto
import com.example.dto.UserIdDto
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory

class UserServiceImpl(private val jwtUserDao: JwtUserDao) : UserService {
    private val logger = LoggerFactory.getLogger(UserServiceImpl::class.java)

    override suspend fun create(jwtUserDto: JwtUserDto): UserIdDto {
        try {
            val user = jwtUserDao.create(
                jwtUserDto.username,
                BCrypt.hashpw(jwtUserDto.password, BCrypt.gensalt())
            )

            return UserIdDto(user.id)
        } catch (e: Exception) {
            logger.error("error while creating user: ${e.message}")

            throw UserCreateException("user already exists")
        }
    }

    override suspend fun get(id: Int): JwtUserPublicDto = jwtUserDao.getById(id)?.let {
        JwtUserPublicDto(it.username)
    } ?: throw UserNotFoundException()
}
