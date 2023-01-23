package org.evcs.android.network.service;

import org.evcs.android.model.Charge;
import org.evcs.android.model.PaginatedResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ChargesServices {
    @GET("https://private-anon-a2cefad69a-evcs5.apiary-mock.com/members/v1/charges")
    Call<PaginatedResponse<Charge>> getCharges(@Query("page") int page, @Query("per_page") int perPage);
}
