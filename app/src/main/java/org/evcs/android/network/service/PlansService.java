package org.evcs.android.network.service;

import org.evcs.android.Configuration;
import org.evcs.android.model.Plan;
import org.evcs.android.model.PlanResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PlansService {

    @GET("/members/v1/plans") //?renewal_period=week
    Call<ArrayList<Plan>> getPlans();

    @GET("/members/v1/plans/index_by_tabs")
    Call<PlanResponse> getPlanTabs();

    @GET(Configuration.PLAN_EXTRAS)
    Call<ArrayList<Plan>> getPlanExtras();

}
