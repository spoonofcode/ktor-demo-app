package com.spoonofcode.routes

import com.spoonofcode.repository.TaskCategoryRepository
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

fun Route.taskCategories(taskRepository: TaskCategoryRepository = get()) {
    crudRoute(
        basePath = "/taskCategories",
        repository = taskRepository,
    )
}