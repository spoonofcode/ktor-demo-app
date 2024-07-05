package com.spoonofcode.di

import com.spoonofcode.dao.TaskDAO
import com.spoonofcode.dao.TaskDAOImpl
import com.spoonofcode.dao.UserDAO
import com.spoonofcode.dao.UserDAOImpl
import com.spoonofcode.new.TaskRepository
import com.spoonofcode.new.UserRepository
import org.koin.dsl.module

val appModule = module {
    single<UserDAO> {
        UserDAOImpl()
    }
    single<TaskDAO> {
        TaskDAOImpl()
    }

    single{
        UserRepository()
    }
    single{
        TaskRepository()
    }
}