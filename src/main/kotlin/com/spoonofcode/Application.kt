package com.spoonofcode

import com.spoonofcode.plugins.*
import io.ktor.server.application.*

//fun main() {
//    embeddedServer(Netty, port = 8100, host = "192.168.0.12", module = Application::module)
//        .start(wait = true)
//}

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


fun Application.module() {
    configureMonitoring()
    configureSerialization()
    configureDI()
    configureDatabases()
    configureRouting()
}
