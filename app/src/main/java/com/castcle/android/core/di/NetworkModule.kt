package com.castcle.android.core.di

import android.content.Context
import com.castcle.android.BuildConfig
import com.castcle.android.R
import com.castcle.android.core.constants.NETWORK_TIMEOUT
import com.castcle.android.core.interceptor.NetworkInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.*

val networkModule = module {
    single {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        OkHttpClient.Builder()
            .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor(get<NetworkInterceptor>())
            .build()
    }
    single {
        val gsonBuilder = GsonBuilder()
            .serializeNulls()
            .setLenient()
            .create()
        Retrofit.Builder()
            .baseUrl(get<Context>().getString(R.string.base_url))
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}