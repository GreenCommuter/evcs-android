package org.evcs.android.network.service;

import androidx.annotation.Nullable;

import org.evcs.android.model.Location;
import org.evcs.android.model.PaginatedResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LocationService {

    @GET("/members/v1/locations")
    Call<PaginatedResponse<Location>> getLocations(@Query("latitude") float latitude,
                                                   @Query("longitude") float longitude,
                                                   @Query("distance") @Nullable Integer distance,
                                                   @Query("connector") @Nullable String connector);

    @GET("/members/v1/locations")
    Call<PaginatedResponse<Location>> getLocations();
}
