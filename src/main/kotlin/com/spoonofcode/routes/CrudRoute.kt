package com.spoonofcode.routes

import com.spoonofcode.repository.CrudRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

internal inline fun <reified RQ : Any, reified RS : Any> Route.crudRoute(
    basePath: String,
    repository: CrudRepository<RQ, RS>,
) {
    route(basePath) {
        post("/") {
            try {
                val newItem = call.receive(RQ::class)
                val createdItem = repository.create(newItem)
                call.respond(HttpStatusCode.Created, "Created Item: $createdItem")
            } catch (e: Throwable) {
                call.respond(HttpStatusCode.BadRequest, errorMessage(e).message ?: "Bad Request")
            }
        }

        get("/{id}") {
            val itemId = call.parameters["id"]?.toIntOrNull()

            if (itemId != null) {
                try {
                    val item = repository.read(itemId)
                    if (item != null) {
                        call.respond(HttpStatusCode.OK, item)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Item not found")
                    }
                } catch (e: Throwable) {
                    call.respond(HttpStatusCode.BadRequest, errorMessage(e).message ?: "Bad Request")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Missing 'id' parameter")
            }
        }

        put("/{id}") {
            val itemId = call.parameters["id"]?.toIntOrNull()

            if (itemId != null) {
                try {
                    val updatedItem = call.receive(RQ::class)
                    val itemUpdated = repository.update(itemId, updatedItem)
                    if (itemUpdated) {
                        call.respond(HttpStatusCode.OK, "Item with ID: $itemId has been updated")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Item not found")
                    }
                } catch (e: Throwable) {
                    call.respond(HttpStatusCode.BadRequest, errorMessage(e).message ?: "Bad Request")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Missing 'id' parameter")
            }
        }

        delete("/{id}") {
            val itemId = call.parameters["id"]?.toIntOrNull()

            if (itemId != null) {
                try {
                    val itemDeleted = repository.delete(itemId)
                    if (itemDeleted) {
                        call.respond(HttpStatusCode.OK, "Item deleted")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Item not found")
                    }
                } catch (e: Throwable) {
                    call.respond(HttpStatusCode.BadRequest, errorMessage(e).message ?: "Bad Request")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Missing 'id' parameter")
            }
        }

        get("/") {
            call.respond(repository.readAll())
        }
    }
}

internal fun errorMessage(e: Throwable): Throwable {
    return when (e) {
        is IllegalArgumentException -> IllegalArgumentException("Invalid Id format")
        else -> e
    }
}