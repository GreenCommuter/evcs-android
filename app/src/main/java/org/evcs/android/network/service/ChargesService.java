package org.evcs.android.network.service;

import org.evcs.android.model.Charge;
import org.evcs.android.model.PaginatedResponse;
import org.evcs.android.model.Session;

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

    //The name is confusing. Here Session = in progress; Charge = Finished
    @GET("/members/v1/charges")
    Call<PaginatedResponse<Charge>> getChargeFromSession(@Query("session_id") int id);
}
