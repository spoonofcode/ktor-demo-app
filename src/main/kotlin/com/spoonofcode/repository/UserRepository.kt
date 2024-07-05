package com.spoonofcode.repository

import com.spoonofcode.data.model.UserRequest
import com.spoonofcode.data.model.UserResponse
import com.spoonofcode.data.model.Users

class UserRepository : GenericCrudRepository<Users, UserRequest, UserResponse>(
    Users,
    { request ->
        mapOf(
            Users.firstName to request.firstName,
            Users.lastName to request.lastName,
        )
    },
    { row ->
        UserResponse(
            id = row[Users.id].value,
            firstName = row[Users.firstName],
            lastName = row[Users.lastName]
        )
    }
)

