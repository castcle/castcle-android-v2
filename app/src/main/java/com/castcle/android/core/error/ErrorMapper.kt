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
            3028 -> CastcleException.EmailHasBeenVerifiedException(throwable.errorMessage)
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
                errorCode = response.code ?: response.statusCode ?: -1,
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