package com.spoonofcode.routes

import com.spoonofcode.dao.UserDAO
import com.spoonofcode.data.model.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.koin.ktor.ext.get

fun Route.users(userDAO: UserDAO = get()) {
    route("/users") {
        get("/") {
            call.respond(userDAO.readAllUsers())
        }

        get("/{id}") {
            val userId = call.parameters["id"]?.toInt()

            if (userId != null) {
                try {
                    val item = userDAO.readUser(userId)
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

        post("/") {
            val newUser = call.receive<User>() // TODO #1 Change to uuid and auto id creation
            userDAO.createUser(newUser.firstName, newUser.lastName)
            call.respond(HttpStatusCode.Created, newUser)
        }

        put("/{id}") {
            val userId = call.parameters["id"]?.toInt()
            if (userId != null) {
                try {
                    val existingUser = userDAO.readUser(userId)
                    if (existingUser != null) {
                        val updatedUser = call.receive<User>()
//                        users[userId-1] = updatedUser.copy(id = userId) // TODO #1 Set proper element index from list
                        call.respond(HttpStatusCode.OK, updatedUser)
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
            val userId = call.parameters["id"]?.toInt()
            if (userId != null) {
                try {
                    val deletedUser = userDAO.deleteUser(userId) // TODO #1 Set proper element index from list
                    if (deletedUser != null) {
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
    }
}