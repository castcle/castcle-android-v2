/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

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

    fun MutableSharedFlow<Unit>.emitOnSuspend() {
        launch { emit(Unit) }
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