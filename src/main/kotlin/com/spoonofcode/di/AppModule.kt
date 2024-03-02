package com.spoonofcode.di

import com.spoonofcode.dao.*
import org.koin.dsl.module

val appModule = module {
    single<UserDAO> {
        UserDAOImpl()
    }
    single<ProfileDAO> {
        ProfileDAOImpl()
    }

    single<TaskDAO> {
        TaskDAOImpl()
    }
}