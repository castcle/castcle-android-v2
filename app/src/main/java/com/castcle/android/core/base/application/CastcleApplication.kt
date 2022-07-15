package com.castcle.android.core.base.application

import android.app.Application
import com.castcle.android.core.di.*
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module
import timber.log.Timber

class CastcleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        initRxErrorHandle()
        initTimber()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@CastcleApplication)
            androidLogger()
            modules(
                ApplicationModule().module,
                applicationModule,
                networkModule,
                storageModule,
            )
        }
    }

    private fun initRxErrorHandle() {
        RxJavaPlugins.setErrorHandler { Timber.e(it) }
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

}