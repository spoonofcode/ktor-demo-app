package com.spoonofcode.plugins

import com.spoonofcode.routes.profiles
import com.spoonofcode.routes.tasks
import com.spoonofcode.routes.users
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        profiles()
        users()
        tasks()
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
