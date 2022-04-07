package com.returnz3ro.messystem.retrofit

import com.returnz3ro.messystem.model.User
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {

    @Headers("Content-Type:application/json")
    @POST("auth_tokens")
    fun LoginService() : Call<User>

}