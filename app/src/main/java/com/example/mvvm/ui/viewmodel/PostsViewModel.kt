package com.example.mvvm.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.data.model.PostsModel
import com.example.mvvm.domain.GetPostsUseCase
import kotlinx.coroutines.launch

class PostsViewModel : ViewModel() {

    val postsModel = MutableLiveData<List<PostsModel>>()
    val isLoading = MutableLiveData<Boolean>()

    var getPostsUseCase = GetPostsUseCase()

    fun onCreate(id: Int) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getPostsUseCase(id)

            if (!result.isNullOrEmpty()) {
                postsModel.postValue(result)
                isLoading.postValue(false)
            }
        }
    }
}