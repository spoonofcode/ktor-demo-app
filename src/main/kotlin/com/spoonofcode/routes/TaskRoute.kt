package com.spoonofcode.routes

import com.spoonofcode.new.TaskRepository
import com.spoonofcode.new.TaskRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

fun Route.tasks(taskRepository: TaskRepository = get()) {
    route("/tasks") {
        post("/") {
            val newTask = call.receive<TaskRequest>()
            val createdTaskId = taskRepository.create(newTask).id
            call.respond(HttpStatusCode.Created, "Created Task with ID: $createdTaskId")
        }

        get("/{id}") {
            val taskId = call.parameters["id"]?.toIntOrNull()

            if (taskId != null) {
                try {
                    val item = taskRepository.read(taskId)
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

        put("/{id}") {
            val taskId = call.parameters["id"]?.toIntOrNull()
            if (taskId != null) {
                try {
                    val updatedTask = call.receive<TaskRequest>()
                    val taskHasBeenUpdated = taskRepository.update(taskId, updatedTask)
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
            val taskId = call.parameters["id"]?.toIntOrNull()
            if (taskId != null) {
                try {
                    val taskHasBeenDeleted = taskRepository.delete(taskId)
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

        get("/") {
            call.respond(taskRepository.readAll())
        }

        get("/user/{userId}") {
            val userId = call.parameters["userId"]?.toIntOrNull()

            if (userId != null) {
                try {
                    val item = taskRepository.readByUserId(userId)
                    if (item != null) {
                        call.respond(item)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Task by userId = $userId not found")
                    }
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid userId format")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Missing 'userId' parameter")
            }
        }

    }
}