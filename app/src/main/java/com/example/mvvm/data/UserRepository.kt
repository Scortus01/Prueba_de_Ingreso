package com.example.mvvm.data

import com.example.mvvm.data.model.UsersModel
import com.example.mvvm.data.model.UsersProvider
import com.example.mvvm.data.network.UserService

class UserRepository {

    private val api = UserService()

    suspend fun getAllUsers(): List<UsersModel> {
        val response: List<UsersModel> = api.getUsers()
        UsersProvider.users = response
        return response
    }
}