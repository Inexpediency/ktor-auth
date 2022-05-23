package com.example.services

import com.example.JwtConfig
import com.example.dao.JwtUserDao
import com.example.dto.JwtUserDto
import com.example.dto.JwtUserPublicDto
import com.example.dto.TokensDto
import com.example.plugins.AuthenticationException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory
import java.util.*
import javax.crypto.spec.SecretKeySpec

class JwtAuthService(
    private val jwtUserDao: JwtUserDao,
    private val config: JwtConfig
) {
    private val logger = LoggerFactory.getLogger(JwtAuthService::class.java)

    private val signingKey
        get() = SecretKeySpec(config.secretKey.toByteArray(), SignatureAlgorithm.HS256.jcaName)

    suspend fun signIn(jwtUserDto: JwtUserDto): TokensDto? {
        val user = jwtUserDao.getByUsername(jwtUserDto.username) ?: return null
        if (!BCrypt.checkpw(jwtUserDto.password, user.password)) {
            return null
        }

        return TokensDto(generateToken(user.id))
    }

    suspend fun whoami(token: String): JwtUserPublicDto? {
        try {
            val userId = Jwts.parserBuilder().setSigningKey(signingKey).build()
                .parseClaimsJws(token).body["userId"] as Int

            return jwtUserDao.getById(userId)?.let { JwtUserPublicDto(it.username) }
        } catch (e: JwtException) {
            logger.error("error while parsing jwt: $e")

            throw AuthenticationException("invalid token")
        }
    }

    private fun generateToken(userId: Int): String = Jwts.builder()
        .setExpiration(Date(System.currentTimeMillis() + config.ttl))
        .claim("userId", userId)
        .signWith(signingKey, SignatureAlgorithm.HS256)
        .compact()
}
