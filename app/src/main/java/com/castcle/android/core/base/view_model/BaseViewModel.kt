package com.castcle.android.core.base.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    val compositeDisposable = CompositeDisposable()

    fun launch(
        onError: (Throwable) -> Unit = {},
        onLaunch: () -> Unit = {},
        onSuccess: () -> Unit = {},
        suspendBlock: suspend () -> Unit
    ): Job {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            onError(throwable)
            Timber.e(throwable)
        }
        return viewModelScope
            .plus(exceptionHandler)
            .launch(Dispatchers.IO) {
                onLaunch()
                suspendBlock()
                onSuccess()
            }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}