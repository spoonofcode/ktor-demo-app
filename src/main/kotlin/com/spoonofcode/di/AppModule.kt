package com.spoonofcode.di

import com.spoonofcode.dao.ProfileDAO
import com.spoonofcode.dao.ProfileDAOImpl
import com.spoonofcode.dao.UserDAO
import com.spoonofcode.dao.UserDAOImpl
import org.koin.dsl.module

val appModule = module {
    single<UserDAO> {
        UserDAOImpl()
    }
    single<ProfileDAO> {
        ProfileDAOImpl()
    }
}