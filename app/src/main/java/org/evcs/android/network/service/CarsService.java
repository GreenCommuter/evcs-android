package org.evcs.android.network.service;

import org.evcs.android.model.CarMaker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CarsService {

    @GET("/members/v1/cars/manufacturers")
    Call<List<CarMaker>> getCarModels();
}
