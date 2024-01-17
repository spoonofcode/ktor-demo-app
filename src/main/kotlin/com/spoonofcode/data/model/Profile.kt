package com.spoonofcode.data.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Profile(
    val id: Int = 0,
    val description: String,
    val user: User,
)

object Profiles : IntIdTable() {
    val description = varchar("description", 255)
    val userId = reference("user_id", Users.id)
}