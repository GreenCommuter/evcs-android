package org.evcs.android.network.service;

import org.evcs.android.model.SubscriptionStatusWrapper;
import org.evcs.android.model.SubscriptionSurveyRequest;
import org.evcs.android.model.SurveyItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SubscriptionService {

    @GET("/members/v1/survey_items")
    Call<ArrayList<SurveyItem>> getQuestions();

    @POST("/members/v1/subscription_surveys")
    Call<Void> createSubscriptionSurvey(@Body SubscriptionSurveyRequest request);

    @POST("/members/v1/subscriptions/{id}/cancel")
    Call<Void> cancelSubscription(@Query("id") String id);

    //    STATUS_ENDPOINT:
    @GET("/members/v1/users/status")
    Call<SubscriptionStatusWrapper> getStatus();

//    Nombre de la subscription:  STATUS_ENDPOINT -> current_subscription -> plan_name
//    Kwh usage: STATUS_ENDPOINT -> current_subscription -> kwh_usage
//    Renewal date: STATUS_ENDPOINT -> current_subscription -> renewal_date
}
