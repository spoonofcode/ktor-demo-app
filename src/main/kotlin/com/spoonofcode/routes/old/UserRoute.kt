package com.spoonofcode.routes.old

import com.spoonofcode.repository.UserRepository
import com.spoonofcode.data.model.UserRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

fun Route.users(userRepository: UserRepository = get()) {
    route("/users") {
        post("/") {
            val newUser = call.receive<UserRequest>()
            val createdUserId = userRepository.create(newUser).id
            call.respond(HttpStatusCode.Created, "Created User with ID: $createdUserId")
        }

        get("/{id}") {
            val userId = call.parameters["id"]?.toIntOrNull()

            if (userId != null) {
                try {
                    val item = userRepository.read(userId)
                    if (item != null) {
                        call.respond(item)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User not found")
                    }
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid Id format")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Missing 'id' parameter")
            }
        }

        put("/{id}") {
            val userId = call.parameters["id"]?.toIntOrNull()
            if (userId != null) {
                try {
                    val updatedUser = call.receive<UserRequest>()
                    val userHasBeenUpdated = userRepository.update(userId, updatedUser)
                    if (userHasBeenUpdated) {
                        call.respond(HttpStatusCode.OK, "User with ID: $userId has been updated")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User not found")
                    }
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid Id format")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Missing 'id' parameter")
            }
        }

        delete("/{id}") {
            val userId = call.parameters["id"]?.toIntOrNull()
            if (userId != null) {
                try {
                    val userHasBeenDeleted = userRepository.delete(userId)
                    if (userHasBeenDeleted) {
                        call.respond(HttpStatusCode.OK, "User deleted")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User not found")
                    }
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid Id format")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Missing 'id' parameter")
            }
        }

        get("/") {
            call.respond(userRepository.readAll())
        }
    }
}