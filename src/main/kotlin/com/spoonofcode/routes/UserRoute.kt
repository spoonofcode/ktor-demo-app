package com.spoonofcode.routes

import com.spoonofcode.repository.UserRepository
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

fun Route.users(userRepository: UserRepository = get()) {
    crudRoute(
        basePath = "/users",
        repository = userRepository
    ) { e ->
        when (e) {
            is IllegalArgumentException -> IllegalArgumentException("Invalid Id format")
            else -> e
        }
    }
}