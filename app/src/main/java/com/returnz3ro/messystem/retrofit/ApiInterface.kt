package com.returnz3ro.messystem.retrofit

import com.google.gson.JsonObject
import com.returnz3ro.messystem.service.model.ResponseJoborderData
import com.returnz3ro.messystem.service.model.ResponseUserData
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @Headers("accept: application/json",
        "content-type: application/json")
    @POST("/api/login")
    fun requestLogin(
        @Body json: JsonObject
    ) : Call<ResponseUserData>

    @Headers("accept: application/json",
        "content-type: application/json")
    @GET("/api/plan")
    fun requestGetAllList() : Call<ResponseJoborderData>

}