package com.castcle.android.core.di

import com.castcle.android.core.api.*
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    single {
        get<Retrofit>().create(AuthenticationApi::class.java).also {
            get<AuthenticationApiHolder>().api = it
        }
    }
    single {
        get<Retrofit>().create(ContentApi::class.java)
    }
    single {
        get<Retrofit>().create(FeedApi::class.java)
    }
    single {
        get<Retrofit>().create(MetadataApi::class.java)
    }
    single {
        get<Retrofit>().create(NotificationApi::class.java)
    }
    single {
        get<Retrofit>().create(SearchApi::class.java)
    }
    single {
        get<Retrofit>().create(UserApi::class.java)
    }
}