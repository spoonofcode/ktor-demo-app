package com.spoonofcode

import com.spoonofcode.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8100, host = "192.168.0.12", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureMonitoring()
    configureRouting()
}
