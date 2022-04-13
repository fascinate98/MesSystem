package com.returnz3ro.messystem.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.returnz3ro.messystem.model.Joborder
import com.returnz3ro.messystem.retrofit.RetrofitClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel(){
    var joborerLiveData: MutableLiveData<Joborder>? = null

    fun getAllMainData() : LiveData<Joborder>? {
        val call = RetrofitClient.apiInterface.GetAllData()

        call.enqueue(object: Callback<Joborder> {
            override fun onFailure(call: Call<Joborder>, t: Throwable) {
                // TODO("Not yet implemented")
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<Joborder>,
                response: Response<Joborder>
            ) {
                if (response.code() == 200) {

                } else {
                    try {
                        val jObjError =
                            JSONObject(response.errorBody()!!.string())
                    } catch (e: Exception) {

                    }
                }
            }
        })

        return joborerLiveData
    }
}