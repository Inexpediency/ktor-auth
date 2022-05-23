package com.example.services

import com.example.dao.JwtUserDao
import com.example.dto.JwtUserDto
import com.example.dto.JwtUserPublicDto
import com.example.dto.TokensDto
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.mindrot.jbcrypt.BCrypt
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.spec.SecretKeySpec

class JwtAuthService(private val jwtUserDao: JwtUserDao) {
    suspend fun signIn(jwtUserDto: JwtUserDto): TokensDto? {
        val user = jwtUserDao.getByUsername(jwtUserDto.username) ?: return null
        if (!BCrypt.checkpw(jwtUserDto.password, user.password)) {
            return null
        }

        return TokensDto(generateToken(user.id))
    }

    suspend fun whoami(token: String): JwtUserPublicDto? {
        val userId = Jwts.parserBuilder().setSigningKey(SIGNING_KEY).build()
            .parseClaimsJws(token).body["userId"] as Int

        return jwtUserDao.getById(userId)?.let { JwtUserPublicDto(it.username) }
    }

    private fun generateToken(userId: Int): String = Jwts.builder()
        .setExpiration(Date(System.currentTimeMillis() + TTL))
        .claim("userId", userId)
        .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
        .compact()

    companion object {
        private val SIGNING_KEY = SecretKeySpec(
            "djasduwqhdHGSHJDguyg213123asjdajkshdsadkadlasd".toByteArray(),
            SignatureAlgorithm.HS256.jcaName
        )
        private const val TTL = 60000
    }
}
