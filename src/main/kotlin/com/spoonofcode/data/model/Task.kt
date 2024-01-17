package com.spoonofcode.data.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.*

@Serializable
data class Task(
    val id: Int = 0,
    val description: String,
    val date: LocalDateTime,
    val isCompleted: Boolean,
    val category: TaskCategory,
)
