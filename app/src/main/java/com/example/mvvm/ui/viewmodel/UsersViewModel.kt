package com.example.mvvm.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.data.model.UsersModel
import com.example.mvvm.domain.GetUsersUseCase
import kotlinx.coroutines.launch

class UsersViewModel : ViewModel() {

    val usersModel = MutableLiveData<List<UsersModel>>()
    val isLoading = MutableLiveData<Boolean>()

    var getUsersUseCase = GetUsersUseCase()

    fun onCreate() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getUsersUseCase()

            if (!result.isNullOrEmpty()) {
                usersModel.postValue(result!!)
                isLoading.postValue(false)
            }
        }
    }
}