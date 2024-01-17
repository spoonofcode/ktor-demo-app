package com.spoonofcode.dao

import com.spoonofcode.data.model.User
import com.spoonofcode.data.model.Users
import com.spoonofcode.plugins.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

interface UserDAO {
    suspend fun createUser(firstName: String, lastName: String): User?
    suspend fun readUser(id: Int): User?
    suspend fun updateUser(id: Int, firstName: String, lastName: String): Boolean
    suspend fun deleteUser(id: Int): Boolean
    suspend fun readAllUsers(): List<User>
}

class UserDAOImpl : UserDAO {
    override suspend fun createUser(firstName: String, lastName: String): User? = dbQuery {
        val insertStatement = Users.insert {
            it[Users.firstName] = firstName
            it[Users.lastName] = lastName
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
    }

    override suspend fun readUser(id: Int): User? = dbQuery {
        Users
            .select { Users.id eq id }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun updateUser(id: Int, firstName: String, lastName: String): Boolean = dbQuery {
        Users.update({ Users.id eq id }) {
            it[Users.firstName] = firstName
            it[Users.lastName] = lastName
        } > 0
    }

    override suspend fun deleteUser(id: Int): Boolean = dbQuery {
        Users.deleteWhere { Users.id eq id } > 0
    }

    override suspend fun readAllUsers(): List<User> = dbQuery {
        Users.selectAll().map(::resultRowToUser)
    }

    fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id].value,
        firstName = row[Users.firstName],
        lastName = row[Users.lastName],
    )
}