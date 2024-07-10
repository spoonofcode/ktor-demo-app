package com.spoonofcode.plugins

import com.spoonofcode.data.model.TaskCategories
import com.spoonofcode.data.model.Tasks
import com.spoonofcode.data.model.Users
import com.spoonofcode.data.model.updateTaskTrigger
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    val driverClass = environment.config.property("storage.driverClassName").getString()
    val jdbcUrl = environment.config.property("storage.jdbcURL").getString()
    val db = Database.connect(provideDataSource(jdbcUrl, driverClass))
    transaction(db) {
        dropTables()
        SchemaUtils.create(Users, Tasks)
        updateTaskTrigger()
        setExampleData()
    }
}

private fun provideDataSource(url: String, driverClass: String): HikariDataSource {
    val hikariConfig = HikariConfig().apply {
        driverClassName = driverClass
        jdbcUrl = url
        maximumPoolSize = 3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }
    return HikariDataSource(hikariConfig)
}

private fun dropTables() {
    transaction {
        SchemaUtils.drop(Users, Tasks, TaskCategories) // Add all the tables you want to drop here
    }
}

private fun setExampleData() {
    Users.insert {
        it[firstName] = "Leo"
        it[lastName] = "Messi"
    }

    Users.insert {
        it[firstName] = "Christiano"
        it[lastName] = "Ronaldo"
    }

    Users.insert {
        it[firstName] = "Frank"
        it[lastName] = "Lampard"
    }

    TaskCategories.insert {
        it[name] = "Sport"
    }

    TaskCategories.insert {
        it[name] = "Work"
    }

    Tasks.insert {
        it[description] = "Example Task 1"
        it[isCompleted] = false
        it[categoryId] = 1
        it[userId] = 1
    }

    Tasks.insert {
        it[description] = "Example Task 2"
        it[isCompleted] = false
        it[categoryId] = 2
        it[userId] = 1
    }

    Tasks.insert {
        it[description] = "Example Task 3"
        it[isCompleted] = false
        it[categoryId] = 1
        it[userId] = 2
    }

    Tasks.insert {
        it[description] = "Example Task 4"
        it[isCompleted] = false
        it[categoryId] = 2
        it[userId] = 2
    }
}

suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }