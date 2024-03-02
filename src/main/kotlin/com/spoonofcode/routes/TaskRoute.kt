package com.spoonofcode.routes

import com.spoonofcode.dao.TaskDAO
import com.spoonofcode.data.model.TaskRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

fun Route.tasks(taskDAO: TaskDAO = get()) {
    route("/tasks") {
        get("/") {
            call.respond(taskDAO.readAllTasks())
        }

        get("/{id}") {
            val taskId = call.parameters["id"]?.toInt()

            if (taskId != null) {
                try {
                    val item = taskDAO.readTask(taskId)
                    if (item != null) {
                        call.respond(item)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Task not found")
                    }
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid Id format")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Missing 'id' parameter")
            }
        }

        post("/") {
            val newTask = call.receive<TaskRequest>() // TODO #1 Change to uuid and auto id creation
            val createdTaskId = taskDAO.createTask(newTask).value
            call.respond(HttpStatusCode.Created, "Created Task with ID: $createdTaskId")
        }

        put("/{id}") {
            val taskId = call.parameters["id"]?.toInt()
            if (taskId != null) {
                try {
                    val existingTask = taskDAO.readTask(taskId)
                    if (existingTask != null) {
                        val updatedTask = call.receive<TaskRequest>()
//                        tasks[taskId-1] = updatedTask.copy(id = taskId) // TODO #1 Set proper element index from list
                        call.respond(HttpStatusCode.OK, updatedTask)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Task not found")
                    }
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid Id format")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Missing 'id' parameter")
            }
        }

        delete("/{id}") {
            val taskId = call.parameters["id"]?.toInt()
            if (taskId != null) {
                try {
                    val deletedTask = taskDAO.deleteTask(taskId) // TODO #1 Set proper element index from list
                    if (deletedTask != null) {
                        call.respond(HttpStatusCode.OK, "Task deleted")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Task not found")
                    }
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid Id format")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Missing 'id' parameter")
            }
        }
    }
}