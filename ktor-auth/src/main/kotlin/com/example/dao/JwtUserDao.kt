package com.example.dao

import com.example.models.JwtUser

interface JwtUserDao {
    suspend fun create(username: String, password: String): JwtUser
    suspend fun getById(id: Int): JwtUser?
    suspend fun getByUsername(username: String): JwtUser?
}
