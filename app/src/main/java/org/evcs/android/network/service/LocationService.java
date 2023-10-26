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
    Call<PaginatedResponse<Location>> getLocations(@Query("latitude") @Nullable Double latitude,
                                                   @Query("longitude") @Nullable Double longitude,
                                                   @Query("with_coming_soon") @Nullable Boolean comingSoon,
                                                   @Query("min_kw") @Nullable Integer minKw,
                                                   @Query("connectors[]") @Nullable String[] connector);

    @GET("/members/v1/locations")
    Call<PaginatedResponse<Location>> getLocations(@Query("latitude") @Nullable Double latitude,
                                                   @Query("longitude") @Nullable Double longitude,
                                                   @Query("miles") int miles,
                                                   @Query("with_coming_soon") @Nullable Boolean comingSoon,
                                                   @Query("min_kw") @Nullable Integer minKw,
                                                   @Query("connectors[]") @Nullable String[] connector);

    @GET("/members/v1/locations")
    Call<PaginatedResponse<Location>> getLocations(@Query("search") @Nullable String name,
                                                   @Query("latitude") @Nullable Double latitude,
                                                   @Query("longitude") @Nullable Double longitude,
                                                   @Query("with_coming_soon") @Nullable Boolean comingSoon,
                                                   @Query("min_kw") @Nullable Integer minKw,
                                                   @Query("connectors[]") @Nullable String[] connector);

    @GET("/members/v1/locations/{id}")
    Call<Location> getLocation(@Path("id") int id);

}