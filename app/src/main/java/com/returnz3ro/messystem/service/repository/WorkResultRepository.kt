package com.returnz3ro.messystem.service.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.returnz3ro.messystem.retrofit.ApiInterface
import com.returnz3ro.messystem.retrofit.RetrofitInstance
import com.returnz3ro.messystem.service.model.datamodel.Joborder
import com.returnz3ro.messystem.service.model.datamodel.WorkResult
import com.returnz3ro.messystem.service.model.responsemodel.ResponseWorkResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkResultRepository {
    companion object {
        private var workResultRepository: WorkResultRepository? = null
        private var context: Context? = null

        @Synchronized
        @JvmStatic
        fun getInstance(context: Context): WorkResultRepository {
            this.context = context
            if (workResultRepository == null) workResultRepository = WorkResultRepository()
            return workResultRepository!!
        }
    }

    fun workStart(result: WorkResult): Int {
        val apiInterface = RetrofitInstance.getRetrofitInstance()
        val jsonData = Gson().toJson(result)
        val convertedObject: JsonObject = Gson().fromJson(jsonData, JsonObject::class.java)

        Log.d(TAG, jsonData.toString() + "으애애ㅐㅐㅐㅐㅐㅐㅐ")
        var workstartService: ApiInterface = apiInterface.create(ApiInterface::class.java)

        workstartService.requestWorkStart(convertedObject).enqueue(object: Callback<ResponseWorkResult>{
            override fun onFailure(call: Call<ResponseWorkResult>, t: Throwable) {
                //todo 실패처리
                Log.d(TAG,t.toString() + "응애응애 실패야")
            }

            override fun onResponse(call: Call<ResponseWorkResult>, response: Response<ResponseWorkResult>) {
                //todo 성공처리
                Log.d(TAG,"응애응애 성공이야222")
                if(response.isSuccessful.not()){
                    Log.d(TAG,"응애응애 실패야222")
                    return
                }
                response.body()?.let{
                    //body가 있다면 그안에는 bestSellerDto가 들어있을것
                    Log.d(TAG,"여기는 워크 ")
                    //joborderData.postValue(it.result)
                }
            }

        })

        return 0
    }



    fun workFinish(result: WorkResult): Int {
        val apiInterface = RetrofitInstance.getRetrofitInstance()
        val jsonData = Gson().toJson(result)
        val convertedObject: JsonObject = Gson().fromJson(jsonData, JsonObject::class.java)

        Log.d(TAG, jsonData.toString() + "으애애ㅐㅐㅐㅐㅐㅐㅐ")
        var workstartService: ApiInterface = apiInterface.create(ApiInterface::class.java)

        workstartService.requestWorkFinish(convertedObject).enqueue(object: Callback<ResponseWorkResult>{
            override fun onFailure(call: Call<ResponseWorkResult>, t: Throwable) {
                //todo 실패처리
                Log.d(TAG,t.toString() + "응애응애 실패야")
            }

            override fun onResponse(call: Call<ResponseWorkResult>, response: Response<ResponseWorkResult>) {
                //todo 성공처리
                Log.d(TAG,"응애응애 성공이야222")
                if(response.isSuccessful.not()){
                    Log.d(TAG,"응애응애 실패야222")
                    return
                }
                response.body()?.let{
                    //body가 있다면 그안에는 bestSellerDto가 들어있을것
                    Log.d(TAG,"여기는 워크 ")
                    //joborderData.postValue(it.result)
                }
            }

        })

        return 0
    }

}