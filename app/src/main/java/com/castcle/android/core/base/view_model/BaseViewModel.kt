package com.castcle.android.core.base.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    val compositeDisposable = CompositeDisposable()

    fun launch(
        onError: (Throwable) -> Unit = { Timber.e(it) },
        action: suspend () -> Unit
    ): Job {
        return viewModelScope
            .plus(CoroutineExceptionHandler { _, throwable -> onError(throwable) })
            .launch(Dispatchers.IO) { action() }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}