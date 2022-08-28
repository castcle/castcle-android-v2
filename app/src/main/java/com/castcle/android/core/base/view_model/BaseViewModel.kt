package com.castcle.android.core.base.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    val compositeDisposable = CompositeDisposable()

    fun <T> MutableSharedFlow<T>.emitOnSuspend(value: T) {
        launch { emit(value) }
    }

    fun <T> launch(
        onError: (Throwable) -> Unit = {},
        onLaunch: () -> Unit = {},
        onSuccess: (T) -> Unit = {},
        suspendBlock: suspend () -> T
    ): Job {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            onError(throwable)
            Timber.e(throwable)
        }
        return viewModelScope
            .plus(exceptionHandler)
            .launch(Dispatchers.IO) {
                onLaunch()
                onSuccess(suspendBlock())
            }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}