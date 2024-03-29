package org.evcs.android.network.service;

import org.evcs.android.model.Charge;
import org.evcs.android.model.PaginatedResponse;
import org.evcs.android.model.Session;
import org.evcs.android.model.user.RateWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChargesService {

    @GET("/members/v1/charges")
    Call<PaginatedResponse<Charge>> getCharges(@Query("user_id") int userId, @Query("page") int page, @Query("per_page") int perPage);

    @GET("/members/v1/charges/{id}")
    Call<Charge> getCharge(@Path("id") int id);

    @GET("/members/v1/charges/current_session")
    Call<Session> getCurrent();

    @GET("/members/v1/pricing/charges")
    Call<RateWrapper> getPpkWh(@Query("station_id") int stationId);
}
