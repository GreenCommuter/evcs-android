package org.evcs.android.network.service;

import org.evcs.android.model.ClientSecret;
import org.evcs.android.model.PaymentMethod;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PaymentMethodsService {

    @GET("/members/v1/payment_methods")
    Call<List<PaymentMethod>> getPaymentMethods();

    @DELETE("/members/v1/payment_methods/{id}")
    Call<Void> removePaymentMethod(@Path("id") String id);

    @POST("/members/v1/payment_methods/{id}/set_default")
    Call<Void> setDefaultPaymentMethod(@Path("id") String id);

    @POST("/members/v1/payment_methods/setup_intent")
    Call<ClientSecret> getClientSecret();
}
