package com.returnz3ro.messystem.retrofit

import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class RetrofitInstance {
    companion object {

        var BASE_URL: String = "http://http://34.64.240.101/"
        fun getRetrofitInstance(): Retrofit {

            return Retrofit.Builder()
                .baseUrl("http://" + "34.64.240.101" + ":8999/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

    }

}