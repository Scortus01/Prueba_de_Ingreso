package com.example.mvvm.domain

import com.example.mvvm.data.PostRepository
import com.example.mvvm.data.model.PostsModel

class GetPostsUseCase() {
    private val repository = PostRepository()

    suspend operator fun invoke(id: Int): List<PostsModel>? = repository.getAllPost(id)
}