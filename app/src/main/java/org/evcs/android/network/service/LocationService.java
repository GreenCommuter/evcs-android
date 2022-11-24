package org.evcs.android.network.service;

import androidx.annotation.Nullable;

import org.evcs.android.model.ConnectorType;
import org.evcs.android.model.Location;
import org.evcs.android.model.PaginatedResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LocationService {

    @GET("/members/v1/locations")
    Call<PaginatedResponse<Location>> getLocations(@Query("page") int page,
                                                   @Query("latitude") @Nullable Double latitude,
                                                   @Query("longitude") @Nullable Double longitude,
                                                   @Query("min_kw") @Nullable Integer minKw,
                                                   @Query("connector") @Nullable String connector);

    @GET("/members/v1/locations")
    Call<PaginatedResponse<Location>> getLocations(@Query("page") int page,
                                                   @Query("per_page") int perPage,
                                                   @Query("latitude") @Nullable Double latitude,
                                                   @Query("longitude") @Nullable Double longitude);

    @GET("/members/v1/locations/{id}")
    Call <Location> getLocation(@Path("id") int id);

}