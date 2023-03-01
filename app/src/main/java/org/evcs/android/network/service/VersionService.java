package org.evcs.android.network.service;

import org.evcs.android.model.SupportedVersion;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VersionService {

    @GET("/members/v1/versions/supported")
    Call<SupportedVersion> isSupportedVersion(@Query("client") String client, @Query("version_id") String version);
}