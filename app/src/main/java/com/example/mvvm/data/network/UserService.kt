package com.example.mvvm.data.network

import com.example.mvvm.core.RetrofitHelper
import com.example.mvvm.data.model.UsersModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Clase encargada de hacer la instancia del helper de retrofit y obtener los usuarios
 */
class UserService {
    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun getUsers(): List<UsersModel> {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(UserApiClient::class.java).getAllUsers()
            response.body() ?: emptyList()
        }
    }
}