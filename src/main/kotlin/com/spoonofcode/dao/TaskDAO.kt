package com.spoonofcode.dao

import com.spoonofcode.data.model.TaskRequest
import com.spoonofcode.data.model.TaskResponse
import com.spoonofcode.data.model.Tasks
import com.spoonofcode.plugins.dbQuery
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

interface TaskDAO {
    suspend fun createTask(taskRequest: TaskRequest): EntityID<Int>
    suspend fun readTask(id: Int): TaskResponse?
    suspend fun updateTask(id: Int, description: String): Boolean
    suspend fun deleteTask(id: Int): Boolean
    suspend fun readAllTasks(): List<TaskResponse>
}

class TaskDAOImpl : TaskDAO {
    override suspend fun createTask(taskRequest: TaskRequest): EntityID<Int> = dbQuery {
        Tasks.insertAndGetId {
            it[description] = taskRequest.description
        }
    }

    override suspend fun readTask(id: Int): TaskResponse? = dbQuery {
        Tasks
            .selectAll().where { Tasks.id eq id }
            .map(::resultRowToTask)
            .singleOrNull()
    }

    override suspend fun updateTask(id: Int, description: String): Boolean = dbQuery {
        Tasks.update({ Tasks.id eq id }) {
            it[Tasks.description] = description
        } > 0
    }

    override suspend fun deleteTask(id: Int): Boolean = dbQuery {
        Tasks.deleteWhere { Tasks.id eq id } > 0
    }

    override suspend fun readAllTasks(): List<TaskResponse> = dbQuery {
        Tasks.selectAll().map(::resultRowToTask)
    }

    private fun resultRowToTask(row: ResultRow): TaskResponse = TaskResponse(
        id = row[Tasks.id].value,
        description = row[Tasks.description],
        creationDate = row[Tasks.creationDate],
        updateDate = row[Tasks.updateDate],
        isCompleted = row[Tasks.isCompleted],
    )
}