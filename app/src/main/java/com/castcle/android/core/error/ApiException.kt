package com.castcle.android.core.error

import com.castcle.android.BuildConfig

class ApiException(
    val errorCode: Int,
    val errorMessage: String,
    val statusCode: Int,
) : Throwable(
    if (BuildConfig.DEBUG) {
        "$statusCode, $errorCode, $errorMessage"
    } else {
        "$errorMessage [$errorCode]"
    }
)