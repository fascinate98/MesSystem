package com.returnz3ro.messystem.retrofit

import com.google.gson.JsonObject
import com.returnz3ro.messystem.service.model.ResponseData
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @Headers("accept: application/json",
        "content-type: application/json")
    @POST("/data/login")
    fun requestLogin(
        @Body json: JsonObject
    ) : Call<ResponseData>
}