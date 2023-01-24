package org.evcs.android.network.service;

import org.evcs.android.model.ClientSecret;
import org.evcs.android.model.PaymentMethod;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PaymentMethodsService {

    @GET("/members/v1/payment_methods")
    Call<List<PaymentMethod>> getPaymentMethods();

    @GET("https://dev.evcs.com/nahue.php")
    Call<ClientSecret> getClientSecret(@Query("id") String customerId);
}
