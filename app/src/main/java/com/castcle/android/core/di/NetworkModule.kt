package com.castcle.android.core.di

import android.content.Context
import com.castcle.android.BuildConfig
import com.castcle.android.R
import com.castcle.android.core.constants.NETWORK_TIMEOUT
import com.castcle.android.core.interceptor.AuthenticationInterceptor
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
        GsonConverterFactory.create(GsonBuilder().serializeNulls().setLenient().create())
    }
    single {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(get<NetworkInterceptor>())
            .authenticator(get<AuthenticationInterceptor>())
            .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(get<Context>().getString(R.string.base_url))
            .client(get())
            .addConverterFactory(get<GsonConverterFactory>())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}