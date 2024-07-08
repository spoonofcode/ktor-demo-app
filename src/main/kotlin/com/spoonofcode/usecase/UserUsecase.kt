package com.spoonofcode.usecase

import com.spoonofcode.data.model.UserRequest
import com.spoonofcode.data.model.UserResponse
import com.spoonofcode.repository.UserRepository

class UserUsecase(
    private val userRepository: UserRepository,
) {

    suspend fun createNewUser(userRequest: UserRequest): UserResponse = userRepository.create(
        userRequest
    )

}