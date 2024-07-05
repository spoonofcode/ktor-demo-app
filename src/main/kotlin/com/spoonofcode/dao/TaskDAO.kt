package com.spoonofcode.dao

import com.spoonofcode.data.model.*
import com.spoonofcode.plugins.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

interface TaskDAO {
    suspend fun createTask(taskRequest: TaskRequest): TaskResponse
    suspend fun getTask(id: Int): TaskResponse?
    suspend fun getAllTasks(): List<TaskResponse>
    suspend fun getAllTasksByUserId(userId: Int): List<TaskResponse>
    suspend fun updateTask(id: Int, taskRequest: TaskRequest): Boolean
    suspend fun deleteTask(id: Int): Boolean
}

class TaskDAOImpl : TaskDAO {
    override suspend fun createTask(taskRequest: TaskRequest): TaskResponse = dbQuery {
        val taskId = Tasks.insertAndGetId {
            it[description] = taskRequest.description
            it[userId] = taskRequest.userId
        }
        getTask(taskId.value)!!
    }

    override suspend fun getTask(id: Int): TaskResponse? = dbQuery {
        Tasks
            .selectAll().where { Tasks.id eq id }
            .map(::resultRowToTask)
            .singleOrNull()
    }

    override suspend fun getAllTasks(): List<TaskResponse> = dbQuery {
        Tasks.selectAll().map(::resultRowToTask)
    }

    override suspend fun getAllTasksByUserId(userId: Int): List<TaskResponse> = dbQuery {
        Tasks
            .selectAll().where { Tasks.userId eq userId }
            .map(::resultRowToTask)
    }

    override suspend fun updateTask(id: Int, taskRequest: TaskRequest): Boolean = dbQuery {
        Tasks.update({ Tasks.id eq id }) {
            it[description] = taskRequest.description
            it[isCompleted] = taskRequest.isCompleted
            it[userId] = taskRequest.userId
        } > 0
    }

    override suspend fun deleteTask(id: Int): Boolean = dbQuery {
        Tasks.deleteWhere { Tasks.id eq id } > 0
    }

    private fun resultRowToTask(row: ResultRow): TaskResponse = TaskResponse(
        id = row[Tasks.id].value,
        description = row[Tasks.description],
        creationDate = row[Tasks.creationDate],
        updateDate = row[Tasks.updateDate],
        isCompleted = row[Tasks.isCompleted],
//        category = row[Tasks.category],
        userId = row[Tasks.userId].value,
    )
}