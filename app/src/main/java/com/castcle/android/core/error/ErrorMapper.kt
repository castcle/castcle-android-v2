package com.castcle.android.core.error

import com.castcle.android.data.core.entity.ApiErrorPayloadResponse
import com.castcle.android.data.core.entity.ApiErrorResponse
import com.google.gson.*
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorMapper {

    fun map(throwable: Throwable?) = when (throwable) {
        is HttpException,
        is SocketTimeoutException,
        is UnknownHostException -> NetworkException()
        is ApiException -> when (throwable.errorCode) {
            3002 -> CastcleException.IncorrectEmailOrPassword(throwable.errorMessage)
            3003 -> CastcleException.IncorrectEmail(throwable.errorMessage)
            3019 -> CastcleException.SocialMediaAlreadyConnected(throwable.errorMessage)
            4001 -> CastcleException.UserNotFoundException(throwable.errorMessage)
            5003 -> CastcleException.ContentNotFoundException(throwable.errorMessage)
            else -> throwable
        }
        null -> UnknownException()
        else -> throwable
    }

    fun map(responseBody: ResponseBody?): Throwable {
        val apiException = try {
            val gsonDeserializer = JsonDeserializer { json, _, _ ->
                ApiErrorResponse(
                    code = json?.asJsonObject?.get("code")?.asInt,
                    message = json?.asJsonObject?.get("message")?.asString,
                    payload = if (json?.asJsonObject?.get("payload")?.isJsonNull == false) {
                        Gson().fromJson(
                            json.asJsonObject?.get("payload")?.asJsonObject,
                            ApiErrorPayloadResponse::class.java,
                        )
                    } else {
                        null
                    },
                    statusCode = json?.asJsonObject?.get("statusCode")?.asInt,
                )
            }
            val response = GsonBuilder()
                .registerTypeAdapter(ApiErrorResponse::class.java, gsonDeserializer)
                .create()
                .fromJson(responseBody?.string().orEmpty(), ApiErrorResponse::class.java)
            ApiException(
                errorCode = response.code ?: -1,
                errorMessage = response.message ?: "Something went wrong. Please try again later.",
                statusCode = response.statusCode ?: -1,
            )
        } catch (throwable: Throwable) {
            ApiException(
                errorCode = -1,
                errorMessage = throwable.message ?: "Something went wrong. Please try again later.",
                statusCode = -1,
            )
        }
        return map(apiException)
    }

}