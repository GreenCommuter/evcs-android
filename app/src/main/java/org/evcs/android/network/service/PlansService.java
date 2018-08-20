package org.evcs.android.network.service;

import org.evcs.android.model.Plan;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface PlansService {

    @POST("/members/v1/plans")
    Call<ArrayList<Plan>> getPlans();

}
