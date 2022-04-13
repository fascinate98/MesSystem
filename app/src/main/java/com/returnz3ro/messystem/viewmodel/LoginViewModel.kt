package com.returnz3ro.messystem.viewmodel

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.returnz3ro.messystem.model.LoginResPonse
import com.returnz3ro.messystem.retrofit.RetrofitClient
import com.returnz3ro.messystem.service.LoginService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel(val context: Context) : ViewModel() {


    fun loginUser(id: String, password: String): LiveData<LoginResPonse>{
        return LoginService.getInstance(context).login(id, password)
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