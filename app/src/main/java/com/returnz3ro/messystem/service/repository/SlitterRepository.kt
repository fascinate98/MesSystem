package com.returnz3ro.messystem.service.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.returnz3ro.messystem.retrofit.ApiInterface
import com.returnz3ro.messystem.retrofit.RetrofitInstance
import com.returnz3ro.messystem.service.model.datamodel.Slitter
import com.returnz3ro.messystem.service.model.responsemodel.ResponseSlitterData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SlitterRepository {
    companion object {
        private var slitterRepository: SlitterRepository? = null
        private var context: Context? = null

        @Synchronized
        @JvmStatic
        fun getInstance(context: Context): SlitterRepository {
            this.context = context
            if (slitterRepository == null) slitterRepository = SlitterRepository()
            return slitterRepository!!
        }
    }

    fun getSlitterList(): MutableLiveData<List<Slitter>> {
        val slitterData= MutableLiveData<List<Slitter>>()
        val apiInterface = RetrofitInstance.getRetrofitInstance()

        var getAllListService: ApiInterface = apiInterface.create(ApiInterface::class.java)

        getAllListService.requestGetSlitterList().enqueue(object: Callback<ResponseSlitterData> {
            override fun onFailure(call: Call<ResponseSlitterData>, t: Throwable) {
                //fail

            }

            override fun onResponse(call: Call<ResponseSlitterData>, response: Response<ResponseSlitterData>) {
                // success
                if(response.isSuccessful.not()){
                    return
                }
                response.body()?.let{
                    slitterData.postValue(it.slitters)
                }
            }

        })

        return slitterData
    }
}