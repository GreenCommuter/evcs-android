package org.evcs.android.network.service;

import org.evcs.android.model.Plan;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PlansService {

    @GET("/members/v1/plans")
    Call<ArrayList<Plan>> getPlans();

    @GET("/plan_index_view.json")
    Call<ArrayList<Plan>> getPlanExtras();

}
