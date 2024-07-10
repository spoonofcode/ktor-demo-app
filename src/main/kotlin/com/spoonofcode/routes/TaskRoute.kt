package com.spoonofcode.routes

import com.spoonofcode.repository.TaskRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

fun Route.tasks(taskRepository: TaskRepository = get()) {
    val basePath = "/tasks"
    crudRoute(
        basePath = basePath,
        repository = taskRepository,
    )
    route(basePath) {
        get("/user/{userId}") {
            val userId = call.parameters["userId"]?.toIntOrNull()

            if (userId != null) {
                try {
                    val items = taskRepository.readByUserId(userId)
                    if (items.isNotEmpty()) {
                        call.respond(items)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Tasks for userId = $userId not found")
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