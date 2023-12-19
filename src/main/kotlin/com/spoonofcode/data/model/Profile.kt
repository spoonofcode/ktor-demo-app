package com.spoonofcode.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    // TODO #1 Change to uuid and auto id creation
    val id: Int,
    val user: User,
)