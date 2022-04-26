package com.returnz3ro.messystem.retrofit

import com.google.gson.JsonObject
import com.returnz3ro.messystem.service.model.responsemodel.ResponseJoborderData
import com.returnz3ro.messystem.service.model.responsemodel.ResponseSlitterData
import com.returnz3ro.messystem.service.model.responsemodel.ResponseUserData
import com.returnz3ro.messystem.service.model.responsemodel.ResponseWorkResult
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

    @Headers("accept: application/json",
        "content-type: application/json")
    @PUT("/api/performance")
    fun requestWorkFinish(
        @Body json: JsonObject
    ) : Call<ResponseWorkResult>

    @Headers("accept: application/json",
        "content-type: application/json")
    @GET("/api/performance/{joborderJobname}")
    fun requestQr(
        @Path("joborderJobname") jobname: String
    ) : Call<ResponseJoborderData>

}