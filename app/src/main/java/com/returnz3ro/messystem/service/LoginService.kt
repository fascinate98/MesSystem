package com.returnz3ro.messystem.service

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.returnz3ro.messystem.model.LoginResPonse

class LoginService {

    companion object {
        private var loginService: LoginService? = null
        private var context: Context?=null
        @Synchronized
        @JvmStatic
        fun getInstance(context: Context): LoginService {
            this.context=context
            if (loginService == null) loginService = LoginService()
            return loginService!!
        }
    }

    fun login(email: String, password: String): LiveData<LoginResPonse> {
        val loginData= MutableLiveData<LoginResPonse>()
//        val user=MyApp.database?.userDao()?.getUser(email, password)
//        loginData.value=user

        //통신

        return loginData
    }

}