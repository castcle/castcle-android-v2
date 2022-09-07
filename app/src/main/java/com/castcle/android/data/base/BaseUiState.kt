package com.castcle.android.data.base


//  Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
//  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
//
//  This code is free software; you can redistribute it and/or modify it
//  under the terms of the GNU General Public License version 3 only, as
//  published by the Free Software Foundation.
//
//  This code is distributed in the hope that it will be useful, but WITHOUT
//  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
//  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
//  version 3 for more details (a copy is included in the LICENSE file that
//  accompanied this code).
//
//  You should have received a copy of the GNU General Public License version
//  3 along with this work; if not, write to the Free Software Foundation,
//  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
//
//  Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
//  Thailand 10160, or visit www.castcle.com if you need additional information
//  or have any questions.
//
//
//  Created by sklim on 12/7/2022 AD at 13:39.

sealed class BaseUiState<out T>(
    val status: ApiStatus,
    val data: T?,
    val apiException: Throwable?
) {
    object Non : BaseUiState<Nothing>(
        status = ApiStatus.NON,
        data = null,
        apiException = null
    )

    data class Success<out R>(val _data: R) : BaseUiState<R>(
        status = ApiStatus.SUCCESS,
        data = _data,
        apiException = null
    )

    object SuccessNonBody : BaseUiState<Nothing>(
        status = ApiStatus.SUCCESS,
        data = null,
        apiException = null
    )

    data class Error(val exception: Throwable) : BaseUiState<Nothing>(
        status = ApiStatus.ERROR,
        data = null,
        apiException = exception
    )

    data class Loading<out R>(val _data: R?, val isLoading: Boolean) : BaseUiState<R>(
        status = ApiStatus.LOADING,
        data = _data,
        apiException = null
    )
}

enum class ApiStatus {
    NON,
    SUCCESS,
    ERROR,
    LOADING
}
