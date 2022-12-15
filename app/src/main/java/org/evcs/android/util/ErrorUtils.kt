package org.evcs.android.util

import com.google.gson.JsonParser
import okhttp3.ResponseBody
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.serializer.ErrorDeserializer

object ErrorUtils {
    private val sErrorDeserializer = ErrorDeserializer()
    private val sParser = JsonParser()

    fun getError(responseBody: ResponseBody): RequestError {
        try {
            val jsonObject = sParser.parse(responseBody.source().readUtf8()).asJsonObject
            val requestError =
                sErrorDeserializer.deserialize(jsonObject, RequestError::class.java, null)
            return requestError ?: return RequestError.getUnknownError()
        } catch (e: Exception) {
            return RequestError.getUnknownError()
        }
    }
}