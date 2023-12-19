package com.spoonofcode.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    // TODO #1 Change to proper uuid
    val id: String,
    val firstName: String,
    val lastName: String,
)
