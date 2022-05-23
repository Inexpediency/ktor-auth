package com.example.dao

import com.example.StorageConfig
import com.example.models.JwtUsers
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(config: StorageConfig) {
        val database = Database.connect(config.jdbcURL, config.driverClassName)

        transaction(database) {
            SchemaUtils.create(JwtUsers)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
