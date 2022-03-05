package com.example.mvvm.domain

import com.example.mvvm.data.UserRepository
import com.example.mvvm.data.model.UsersModel

class GetUsersUseCase {
    private val repository = UserRepository()

    suspend operator fun invoke(): List<UsersModel>? = repository.getAllUsers()
}