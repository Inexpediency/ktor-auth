package com.example.models

import org.jetbrains.exposed.sql.Table

data class JwtUser(val id: Int, val username: String, val password: String)

object JwtUsers : Table() {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 128).uniqueIndex()
    val password = varchar("password", 128)

    override val primaryKey = PrimaryKey(id)
}
