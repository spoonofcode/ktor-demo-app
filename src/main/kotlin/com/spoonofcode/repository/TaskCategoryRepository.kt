package com.spoonofcode.repository

import com.spoonofcode.data.model.TaskCategories
import com.spoonofcode.data.model.TaskCategoryRequest
import com.spoonofcode.data.model.TaskCategoryResponse

class TaskCategoryRepository : GenericCrudRepository<TaskCategories, TaskCategoryRequest, TaskCategoryResponse>(
    TaskCategories,
    { request ->
        mapOf(
            TaskCategories.name to request.name,
        )
    },
    { row ->
        TaskCategoryResponse(
            id = row[TaskCategories.id].value,
            name = row[TaskCategories.name],
        )
    }
)