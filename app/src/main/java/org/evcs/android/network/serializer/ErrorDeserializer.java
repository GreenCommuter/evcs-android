package org.evcs.android.network.serializer;

import com.base.networking.retrofit.serializer.BaseGsonBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import org.evcs.android.model.shared.RequestError;
import org.joda.time.DateTime;

import java.lang.reflect.Type;

public class ErrorDeserializer implements JsonDeserializer<RequestError> {
    @Override
    public RequestError deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        Gson gson = BaseGsonBuilder.getBaseGsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeDeserializer()).create();
        return gson.fromJson(json, typeOfT);
    }
}
