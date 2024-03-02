package com.spoonofcode.data.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class TaskCategory(
    val id: Int = 0,
    val name: String,
)

object TaskCategories : IntIdTable() {
    val name = varchar("name", 128)
}