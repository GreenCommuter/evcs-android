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
    Call<PaginatedResponse<Location>> getLocations(@Query("page") int page,
                                                   @Query("latitude") float latitude,
                                                   @Query("longitude") float longitude);
//                                                 @Query("distance") @Nullable Integer distance


    @GET("/members/v1/locations")
    Call<PaginatedResponse<Location>> getLocations(@Query("page") int page,
                                                   @Query("latitude") float latitude,
                                                   @Query("longitude") float longitude,
                                                   @Query("min_kw") @Nullable Integer minKw,
                                                   @Query("connector") @Nullable String connector);

    @GET("/members/v1/locations")
    Call<PaginatedResponse<Location>> getLocations(@Query("page") int page);
}
