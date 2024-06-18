package org.evcs.android.network.service;

import org.evcs.android.model.PaginatedResponse;
import org.evcs.android.model.Payment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PaymentsService {

    @GET("/members/v1/payments")
    Call<PaginatedResponse<Payment>> getPayments(@Query("page") int page, @Query("per_page") int perPage);

    @GET("/members/v1/payments/rejected")
    Call<ArrayList<Payment>> getRejectedPayments();

    @GET("/members/v1/payments/{id}/run")
    Call<Void> processPayment(@Path("id") String id);
}
