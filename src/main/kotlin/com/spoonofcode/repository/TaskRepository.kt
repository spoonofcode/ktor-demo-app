package com.spoonofcode.repository

import com.spoonofcode.data.model.TaskRequest
import com.spoonofcode.data.model.TaskResponse
import com.spoonofcode.data.model.Tasks
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class TaskRepository : GenericCrudRepository<Tasks, TaskRequest, TaskResponse>(
    Tasks,
    { request ->
        mapOf(
            Tasks.description to request.description,
            Tasks.isCompleted to request.isCompleted,
            Tasks.userId to request.userId
        )
    },
    { row ->
        TaskResponse(
            id = row[Tasks.id].value,
            description = row[Tasks.description],
            creationDate = row[Tasks.creationDate],
            updateDate = row[Tasks.updateDate],
            isCompleted = row[Tasks.isCompleted],
            categoryId = row[Tasks.categoryId].value,
            userId = row[Tasks.userId].value,
        )
    }
) {
    fun readByUserId(userId: Int): List<TaskResponse> {
        return transaction {
            Tasks.select { Tasks.userId eq userId }.map { toResponse(it) }
        }
    }
}