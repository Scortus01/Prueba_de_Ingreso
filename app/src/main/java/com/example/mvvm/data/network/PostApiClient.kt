package com.example.mvvm.data.network

import com.example.mvvm.data.model.PostsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface encargada de hacer el consumo del API Rest de las publicaciones especificas de cada usuario
 */
interface PostApiClient {
    @GET("/posts")
    suspend fun getAllPosts(@Query("userId") id: Int): Response<List<PostsModel>>
}