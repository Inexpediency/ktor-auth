package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.models.JwtUser
import com.example.models.JwtUsers
import com.example.services.UserCreateException
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class JwtUserDaoImpl : JwtUserDao {
    override suspend fun create(username: String, password: String): JwtUser = dbQuery {
        val insertStatement = JwtUsers.insert {
            it[JwtUsers.username] = username
            it[JwtUsers.password] = password
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToJwtUser) ?: throw UserCreateException()
    }

    override suspend fun getById(id: Int): JwtUser? = dbQuery {
        JwtUsers
            .select { JwtUsers.id eq id }
            .map(::resultRowToJwtUser)
            .singleOrNull()
    }

    override suspend fun getByUsername(username: String): JwtUser? = dbQuery {
        JwtUsers
            .select { JwtUsers.username eq username }
            .map(::resultRowToJwtUser)
            .singleOrNull()
    }

    private fun resultRowToJwtUser(row: ResultRow): JwtUser = JwtUser(
        id = row[JwtUsers.id],
        username = row[JwtUsers.username],
        password = row[JwtUsers.password]
    )
}
