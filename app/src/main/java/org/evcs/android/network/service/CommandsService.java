package org.evcs.android.network.service;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface CommandsService {

    @POST("/members/v1/commands/start_session")
    Call<Void> startSession(@Query("connector_id") int connectorId,
                            @Query("payment_method") String paymentMethodId,
                            @Query("coupon_codes") ArrayList<String> couponCodes);

    @GET
    Call<Object> startSession(@Url String url);

    @POST("/members/v1/commands/stop_session")
    Call<Void> stopSession(@Query("session_id") String sessionId);

}
