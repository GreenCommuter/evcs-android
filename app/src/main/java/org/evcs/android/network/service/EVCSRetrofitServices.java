package org.evcs.android.network.service;

import androidx.annotation.NonNull;

import com.base.networking.retrofit.RetrofitServices;
import com.google.gson.GsonBuilder;

import org.evcs.android.Configuration;
import org.evcs.android.network.interceptor.SecuredRequestInterceptor;
import org.evcs.android.network.serializer.DateTimeDeserializer;
import org.joda.time.DateTime;

import okhttp3.OkHttpClient;

public class EVCSRetrofitServices extends RetrofitServices {

    @Override
    @NonNull
    public String getApiEndpoint() {
        return Configuration.API_ENDPOINT;
    }

    @Override
    protected void initClient(@NonNull OkHttpClient.Builder builder) {
        super.initClient(builder);
        builder.addInterceptor(new SecuredRequestInterceptor());
    }

    @Override
    protected void initGson(@NonNull GsonBuilder builder) {
        builder.registerTypeAdapter(DateTime.class, new DateTimeDeserializer());
    }

}
