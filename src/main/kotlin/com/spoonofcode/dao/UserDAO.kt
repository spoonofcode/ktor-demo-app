package com.spoonofcode.dao

import com.spoonofcode.data.model.UserRequest
import com.spoonofcode.data.model.UserResponse
import com.spoonofcode.data.model.Users
import com.spoonofcode.plugins.dbQuery
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

interface UserDAO {
    suspend fun createUser(userRequest: UserRequest): EntityID<Int>
    suspend fun getUser(id: Int): UserResponse?
    suspend fun getAllUsers(): List<UserResponse>
    suspend fun updateUser(id: Int, userRequest: UserRequest): Boolean
    suspend fun deleteUser(id: Int): Boolean
}

class UserDAOImpl : UserDAO {
    override suspend fun createUser(userRequest: UserRequest): EntityID<Int> = dbQuery {
        Users.insertAndGetId {
            it[firstName] = userRequest.firstName
            it[lastName] = userRequest.lastName
        }
    }

    override suspend fun getUser(id: Int): UserResponse? = dbQuery {
        Users
            .selectAll().where { Users.id eq id }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun getAllUsers(): List<UserResponse> = dbQuery {
        Users.selectAll().map(::resultRowToUser)
    }


    override suspend fun updateUser(id: Int, userRequest: UserRequest): Boolean = dbQuery {
        Users.update({ Users.id eq id }) {
            it[firstName] = firstName
            it[lastName] = userRequest.lastName
        } > 0
    }

    override suspend fun deleteUser(id: Int): Boolean = dbQuery {
        Users.deleteWhere { Users.id eq id } > 0
    }

    private fun resultRowToUser(row: ResultRow): UserResponse = UserResponse(
        id = row[Users.id].value,
        firstName = row[Users.firstName],
        lastName = row[Users.lastName],
    )
}