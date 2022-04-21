package com.returnz3ro.messystem.retrofit

import com.google.gson.JsonObject
import com.returnz3ro.messystem.service.model.ResponseJoborderData
import com.returnz3ro.messystem.service.model.ResponseSlitterData
import com.returnz3ro.messystem.service.model.ResponseUserData
import com.returnz3ro.messystem.service.model.ResponseWorkResult
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

    @Headers("accept: application/json",
        "content-type: application/json")
    @GET("/api/getslitter")
    fun requestGetSlitterList() : Call<ResponseSlitterData>

    @Headers("accept: application/json",
        "content-type: application/json")
    @POST("/api/performance")
    fun requestWorkStart(
        @Body json: JsonObject
    ) : Call<ResponseWorkResult>

}