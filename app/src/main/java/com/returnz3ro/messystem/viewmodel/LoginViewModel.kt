package com.returnz3ro.messystem.viewmodel

import android.content.Context
import android.os.UserManager
import androidx.datastore.core.DataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.returnz3ro.messystem.service.model.datamodel.User
import com.returnz3ro.messystem.service.model.datastore.DataStoreModule
import com.returnz3ro.messystem.service.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.prefs.Preferences

@HiltViewModel
class LoginViewModel(val context: Context) : ViewModel() {
    lateinit var userInfo: LiveData<User>

    fun loginService(id: String, password: String): LiveData<User>?{
        userInfo = UserRepository.getInstance(context).login(id, password)
        return userInfo
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