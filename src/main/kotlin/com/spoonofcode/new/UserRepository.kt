package com.spoonofcode.new

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

