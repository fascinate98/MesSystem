package com.returnz3ro.messystem.service.repository

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.returnz3ro.messystem.retrofit.ApiInterface
import com.returnz3ro.messystem.retrofit.RetrofitInstance
import com.returnz3ro.messystem.service.model.Joborder
import com.returnz3ro.messystem.service.model.ResponseJoborderData
import com.returnz3ro.messystem.service.model.ResponseUserData
import com.returnz3ro.messystem.service.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoborderRepository {
    companion object {
        private var joborderRepository: JoborderRepository? = null
        private var context: Context? = null

        @Synchronized
        @JvmStatic
        fun getInstance(context: Context): JoborderRepository {
            this.context = context
            if (joborderRepository == null) joborderRepository = JoborderRepository()
            return joborderRepository!!
        }
    }

    fun getJoborderList(): MutableLiveData<List<Joborder>> {
        val joborerData= MutableLiveData<List<Joborder>>()
        val apiInterface = RetrofitInstance.getRetrofitInstance()

        Log.d(ContentValues.TAG, "으애애ㅐㅐㅐㅐㅐㅐㅐ")
        var getAllListService: ApiInterface = apiInterface.create(ApiInterface::class.java)

        getAllListService.requestGetAllList().enqueue(object: Callback<ResponseJoborderData> {
            override fun onFailure(call: Call<ResponseJoborderData>, t: Throwable) {
                //todo 실패처리
                Log.d(ContentValues.TAG,t.toString() + "응애응애 실패야")
            }

            override fun onResponse(call: Call<ResponseJoborderData>, response: Response<ResponseJoborderData>) {
                //todo 성공처리
                Log.d(ContentValues.TAG,"응애응애 성공이야222")
                if(response.isSuccessful.not()){
                    Log.d(ContentValues.TAG,"응애응애 실패야222")
                    return
                }
                response.body()?.let{
                    //body가 있다면 그안에는 bestSellerDto가 들어있을것

                    it.joborders.forEach{ joborder->
                        Log.d(TAG,joborder.toString())

                    }
                    joborerData.postValue(it.joborders)

                }
            }

        })

        return joborerData
    }
}