package org.evcs.android.network.service;

import org.evcs.android.model.user.BooleanWrapper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MessageService {
    @POST("/members/v1/messages/marketing_notifications")
    Call<Void> enableNotifications(@Body BooleanWrapper enabled);

    //The main difference being that this triggers an email
    @POST("/members/v1/messages/opt_out")
    Call<Void> optOut();
}
