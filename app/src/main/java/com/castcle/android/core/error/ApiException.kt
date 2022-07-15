package com.castcle.android.core.error

import com.castcle.android.data.core.entity.ApiErrorPayloadResponse
import com.castcle.android.data.core.entity.ApiErrorResponse
import okhttp3.ResponseBody

class ApiException(
    val code: Int? = null,
    val error: String? = null,
    val errorMessage: String? = null,
    val payload: ApiErrorPayloadResponse? = null,
    val raw: String? = null,
    val statusCode: Int? = null,
) : Throwable(errorMessage) {

    companion object {
        fun map(errorBody: ResponseBody?): ApiException {
            val response = ApiErrorResponse.map(errorBody)
            return ApiException(
                code = response.code,
                error = response.error,
                errorMessage = response.message,
                payload = response.payload,
                raw = response.raw,
                statusCode = response.statusCode,
            )
        }
    }

}