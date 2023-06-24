package org.evcs.android.network.service;

import org.evcs.android.model.Subscription;
import org.evcs.android.model.SubscriptionRequest;
import org.evcs.android.model.SubscriptionStatusWrapper;
import org.evcs.android.model.SubscriptionSurveyRequest;
import org.evcs.android.model.SurveyItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SubscriptionService {

    @GET("/members/v1/survey_items")
    Call<ArrayList<SurveyItem>> getQuestions();

    @POST("/members/v1/subscription_surveys")
    Call<Void> createSubscriptionSurvey(@Body SubscriptionSurveyRequest request);

    @POST("/members/v1/subscriptions")
    Call<Subscription> createSubscription(@Body SubscriptionRequest subscriptionRequest);

    @POST("/members/v1/subscriptions/{id}/change_plan")
    Call<Subscription> changePlan(@Path("id") String subscriptionId, @Body SubscriptionRequest subscriptionRequest);

    @POST("/members/v1/subscriptions/{id}/cancel")
    Call<Void> cancelSubscription(@Path("id") String id);

    //    STATUS_ENDPOINT:
    @GET("/members/v1/users/status")
    Call<SubscriptionStatusWrapper> getStatus();

    @POST("/members/v1/subscriptions/{id}/void_cancelation")
    Call<Void> voidSubscriptionCancelation(@Path("id") String id);

//    Nombre de la subscription:  STATUS_ENDPOINT -> current_subscription -> plan_name
//    Kwh usage: STATUS_ENDPOINT -> current_subscription -> kwh_usage
//    Renewal date: STATUS_ENDPOINT -> current_subscription -> renewal_date
}
