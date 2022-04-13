package com.returnz3ro.messystem.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.returnz3ro.messystem.service.model.User
import com.returnz3ro.messystem.service.repository.UserRepository


class LoginViewModel(val context: Context) : ViewModel() {

    fun loginRepository(id: String, password: String): LiveData<User>?{
        return UserRepository.getInstance(context).login(id, password)
    }

    override fun onCleared() {
        super.onCleared()
    }

    class Factory(val context: Context) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(context) as T
        }
    }
}