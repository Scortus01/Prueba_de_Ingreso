package com.example.mvvm.data.network

import com.example.mvvm.data.model.UsersModel
import retrofit2.Response
import retrofit2.http.GET

/**
 * Interface encargada de hacer el consumo del API Rest de los usuarios
 */
interface UserApiClient {
    @GET("/users")
    suspend fun getAllUsers(): Response<List<UsersModel>>
}