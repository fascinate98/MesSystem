package com.returnz3ro.messystem.viewmodel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.returnz3ro.messystem.model.User
import android.util.Log
import com.returnz3ro.messystem.retrofit.RetrofitClient
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class LoginViewModel : ViewModel() {
    var userLiveData: MutableLiveData<User>? = null

    fun loginUser(email: String, password: String) : LiveData<User>? {
        val call = RetrofitClient.apiInterface.LoginService()

        call.enqueue(object: Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                // TODO("Not yet implemented")
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<User>,
                response: Response<User>
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

        return userLiveData
    }
}