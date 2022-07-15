package com.castcle.android.core.di

import androidx.room.Room
import com.castcle.android.core.constants.DATABASE_NAME
import com.castcle.android.core.storage.database.CastcleDatabase
import org.koin.dsl.module

val storageModule = module {
    single {
        Room.databaseBuilder(get(), CastcleDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}