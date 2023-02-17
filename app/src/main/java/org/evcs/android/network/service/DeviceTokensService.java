package org.evcs.android.network.service;

import org.evcs.android.model.push.DeviceToken;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Service that handles requests regarding the token. We use this to notify API the token for the
 * logged user.
 */
public interface DeviceTokensService {
    @POST("/members/v1/save_token")
    Call<Void> addDeviceToken(@Body DeviceToken deviceToken);
}
