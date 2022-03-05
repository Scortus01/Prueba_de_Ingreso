package com.example.mvvm.data

import com.example.mvvm.data.model.PostsModel
import com.example.mvvm.data.model.PostsProvider
import com.example.mvvm.data.network.PostService

/**
 * Clase encargada de hacer la instancia del servicio de publicaciones y obtener publicaciones
 */
class PostRepository {

    private val api = PostService()

    suspend fun getAllPost(id: Int): List<PostsModel> {
        val response: List<PostsModel> = api.getPosts(id)
        PostsProvider.posts = response
        return response
    }
}