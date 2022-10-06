package org.evcs.android.network.serializer;

import com.base.networking.retrofit.serializer.BaseGsonBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class GenericListDeserializer {
    /**
     * Takes a jsonList, returns a list of parameterizedType
     */
    public static <T> List<T> deserialize(String json, Class<T> parameterizedType) {
        Type type = TypeToken.getParameterized(List.class, parameterizedType).getType();
        Gson gson = BaseGsonBuilder.getBaseGsonBuilder().create();
        List<T> castResponse = gson.fromJson(json, type);
        return castResponse;
    }

}
