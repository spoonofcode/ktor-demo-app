package com.spoonofcode.data.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class TaskCategoryRequest(
    val name: String,
)

@Serializable
data class TaskCategoryResponse(
    val id: Int,
    val name: String,
)

object TaskCategories : IntIdTable() {
    val name = varchar("name", 128)
}