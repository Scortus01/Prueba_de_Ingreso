package com.example.mvvm.data.network

import com.example.mvvm.data.model.PostsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PostApiClient {
    @GET("/posts")
    suspend fun getAllPosts(@Query("userId") id: Int): Response<List<PostsModel>>
}