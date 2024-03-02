package com.spoonofcode.data.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

@Serializable
data class TaskRequest(
    val description: String,
//    val category: TaskCategory,
//    val userId: Int,
)

@Serializable
data class TaskResponse(
    val id: Int,
    val description: String,
    val creationDate: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    val updateDate: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    val isCompleted: Boolean,
//    val category: TaskCategory,
//    val userId: Int,
)

object Tasks : IntIdTable() {
    val description = varchar("description", 128)
    val creationDate = datetime("creation_date").defaultExpression(CurrentDateTime)
    val updateDate = datetime("update_date").defaultExpression(CurrentDateTime)
    val isCompleted = bool("is_completed").default(false)
//    val category = reference("category_id", TaskCategories)
//    val userId = reference("user_id", Users)
}