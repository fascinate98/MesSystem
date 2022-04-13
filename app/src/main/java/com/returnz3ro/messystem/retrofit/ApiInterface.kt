package com.returnz3ro.messystem.retrofit

import com.returnz3ro.messystem.service.model.ResponseData
import com.returnz3ro.messystem.service.model.User
import com.returnz3ro.messystem.service.model.UserRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @Headers("accept: application/json",
        "content-type: application/json")
    @POST("/data/login")
    fun requestLogin(
        @Body request: UserRequest)
    : Call<ResponseData>
}