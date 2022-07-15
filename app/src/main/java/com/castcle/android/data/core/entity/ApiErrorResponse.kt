package com.castcle.android.data.core.entity

import androidx.annotation.Keep
import com.google.gson.*
import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import java.lang.reflect.Type

@Keep
data class ApiErrorResponse(
    @SerializedName("code") val code: Int? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("payload") val payload: ApiErrorPayloadResponse? = null,
    @SerializedName("raw") val raw: String? = null,
    @SerializedName("statusCode") val statusCode: Int? = null,
) {

    companion object {
        fun map(errorBody: ResponseBody?): ApiErrorResponse {
            try {
                val jsonString = errorBody?.string() ?: return ApiErrorResponse()
                val deserializer = object : JsonDeserializer<ApiErrorResponse> {
                    override fun deserialize(
                        json: JsonElement?,
                        typeOfT: Type?,
                        context: JsonDeserializationContext?
                    ): ApiErrorResponse {
                        val response = json?.asJsonObject ?: return ApiErrorResponse()
                        return ApiErrorResponse(
                            code = response.get("code")?.asInt,
                            error = response.get("error")?.asString,
                            message = response.get("message")?.asString,
                            payload = if (response.get("payload")?.isJsonNull == false) {
                                Gson().fromJson(
                                    response.get("payload")?.asJsonObject,
                                    ApiErrorPayloadResponse::class.java,
                                )
                            } else {
                                null
                            },
                            raw = jsonString,
                            statusCode = response.get("statusCode")?.asInt,
                        )
                    }
                }
                return GsonBuilder().registerTypeAdapter(ApiErrorResponse::class.java, deserializer)
                    .create()
                    .fromJson(jsonString, ApiErrorResponse::class.java)
            } catch (exception: Exception) {
                return ApiErrorResponse()
            }
        }
    }

}