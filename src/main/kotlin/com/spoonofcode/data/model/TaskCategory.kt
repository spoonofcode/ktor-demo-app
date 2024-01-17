package com.spoonofcode.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TaskCategory (
    val id: Int = 0,
    val name: String,
)