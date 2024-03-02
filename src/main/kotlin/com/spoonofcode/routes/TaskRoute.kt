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
            val newTask = call.receive<TaskRequest>()
            val createdTaskId = taskDAO.createTask(newTask).value
            call.respond(HttpStatusCode.Created, "Created Task with ID: $createdTaskId")
        }

        put("/{id}") {
            val taskId = call.parameters["id"]?.toInt()
            if (taskId != null) {
                try {
                    val updatedTask = call.receive<TaskRequest>()
                    val taskHasBeenUpdated = taskDAO.updateTask(taskId, updatedTask)
                    if (taskHasBeenUpdated) {
                        call.respond(HttpStatusCode.OK, "Task with ID: $taskId has been updated")
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
                    val taskHasBeenDeleted = taskDAO.deleteTask(taskId)
                    if (taskHasBeenDeleted) {
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