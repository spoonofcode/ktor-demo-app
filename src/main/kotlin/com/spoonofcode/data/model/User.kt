package com.spoonofcode.data.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class UserRequest(
    val firstName: String,
    val lastName: String,
)

@Serializable
data class UserResponse(
    val id: Int,
    val firstName: String,
    val lastName: String,
)

object Users : IntIdTable() {
    val firstName = varchar("first_name", 128)
    val lastName = varchar("last_name", 128)
}