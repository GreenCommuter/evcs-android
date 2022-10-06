package org.evcs.android.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.evcs.android.model.shared.RequestError;
import org.evcs.android.network.serializer.ErrorDeserializer;

import okhttp3.ResponseBody;

public class ErrorUtils {

    private static final String POP_UP_KEY = "pop_up";
    private static final String ERRORS_KEY = "errors";
    private static ErrorDeserializer sErrorDeserializer = new ErrorDeserializer();
    private static JsonParser sParser = new JsonParser();

    public static RequestError getError(ResponseBody responseBody) {
        try {
            JsonObject jsonObject = sParser.parse(responseBody.source().readUtf8()).getAsJsonObject();
            JsonElement popUpJson = jsonObject.get(POP_UP_KEY);
            if (popUpJson == null) {
                JsonElement error = jsonObject.get(ERRORS_KEY);
                if (error.isJsonObject()) {
                    popUpJson = error.getAsJsonObject().get(POP_UP_KEY);
                } else {
                    return new RequestError(error.getAsString());
                }
            }
            RequestError requestError = sErrorDeserializer.deserialize(popUpJson, RequestError.class,null);
            if (requestError == null) return RequestError.getUnknownError();
            return requestError;
        } catch (Exception e) {
            return RequestError.getUnknownError();
        }
    }

}
