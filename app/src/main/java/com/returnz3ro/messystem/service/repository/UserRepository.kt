package com.returnz3ro.messystem.service.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.returnz3ro.messystem.retrofit.ApiInterface
import com.returnz3ro.messystem.retrofit.RetrofitInstance
import com.returnz3ro.messystem.service.model.responsemodel.ResponseUserData
import com.returnz3ro.messystem.service.model.datamodel.User
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class UserRepository {
    companion object {
        private var loginRepository: UserRepository? = null
        private var context: Context? = null

        @Synchronized
        @JvmStatic
        fun getInstance(context: Context): UserRepository {
            this.context = context
            if (loginRepository == null) loginRepository = UserRepository()
            return loginRepository!!
        }
    }

    fun login(userId: String, userPw: String): LiveData<User> {
        val loginData= MutableLiveData<User>()
        val apiInterface = RetrofitInstance.getRetrofitInstance()
        val jsonData: JsonObject = JsonObject().apply {
            //loginData.value?.userId.toString()
            addProperty("userId", userId)
            addProperty("userPw", userPw)
        }


        var loginService: ApiInterface = apiInterface.create(ApiInterface::class.java)

        loginService.requestLogin(jsonData).enqueue(object: Callback<ResponseUserData>{
            override fun onFailure(call: Call<ResponseUserData>, t: Throwable) {
                //todo 실패처리
                Log.d(TAG,t.toString() + "응애응애 실패야")
            }

            override fun onResponse(call: Call<ResponseUserData>, response: Response<ResponseUserData>) {
                //todo 성공처리
                Log.d(TAG,"응애응애 성공이야222")
                if(response.isSuccessful.not()){
                    Log.d(TAG,"응애응애 실패야222")
                    return
                }
                response.body()?.let{
                    //body가 있다면 그안에는 bestSellerDto가 들어있을것
                    Log.d(TAG,it.user.toString() + "dfdfd")
                    loginData.postValue(it.user)
                }
            }

        })

        return loginData
    }
}