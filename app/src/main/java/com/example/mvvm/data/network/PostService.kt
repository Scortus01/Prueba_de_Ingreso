package com.example.mvvm.data.network

import com.example.mvvm.core.RetrofitHelper
import com.example.mvvm.data.model.PostsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostService {
    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun getPosts(id : Int): List<PostsModel> {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(PostApiClient::class.java).getAllPosts(id)
            response.body() ?: emptyList()
        }
    }
}