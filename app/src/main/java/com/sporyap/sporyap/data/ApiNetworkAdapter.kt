package com.sporyap.sporyap.data

import com.google.gson.GsonBuilder
import com.sporyap.sporyap.utils.Constants
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiNetworkAdapter {

    fun appApi(): AppApi{
        val builder = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .certificatePinner(CertificatePinner.DEFAULT)
            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(Interceptor{ chain ->
                val request = chain.request().newBuilder()
                    .addHeader(Constants.CONTENT_TYPE_KEY , Constants.CONTENT_TYPE_VALUE)
                    .build()
                chain.proceed(request)
            })

        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.BASE_URL)
            .client(builder.build())
            .build()

        return retrofit.create(AppApi::class.java)
    }
}