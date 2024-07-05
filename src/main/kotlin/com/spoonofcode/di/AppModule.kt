package com.spoonofcode.di

import com.spoonofcode.repository.TaskRepository
import com.spoonofcode.repository.UserRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::UserRepository)
    singleOf(::TaskRepository)
}