package org.evcs.android.network.service;

import org.evcs.android.model.PaginatedResponse;
import org.evcs.android.model.Station;
import org.evcs.android.model.SubscriptionStatus;
import org.evcs.android.model.SubscriptionStatusWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StationsService {

    @GET("/members/v1/stations")
    Call<PaginatedResponse<Station>> getStation(@Query("name") int id);

//    STATUS_ENDPOINT:
    @GET("/members/v1/users/status")
    Call<SubscriptionStatusWrapper> getStatus();

//    Nombre de la subscription:  STATUS_ENDPOINT -> current_subscription -> plan_name
//    Kwh usage: STATUS_ENDPOINT -> current_subscription -> kwh_usage
//    Renewal date: STATUS_ENDPOINT -> current_subscription -> renewal_date
//    Charge rate (precio por kwh): STATION_ENDPOINT -> pricing -> detail -> price_kwh
//    Charge rate (precio por hora): STATION_ENDPOINT -> pricing -> detail -> thereafter_price (price_kwh es null)
//    Charge rate (charging free at location): STATION_ENDPOINT -> pricing -> detail -> Mostrar en el prompt “free_charging_code” siendo show_free_charging_code == TRUE
//    Vistas de PAY AS YOU GO: STATUS_ENDPOINT -> current_subscription es NULL
//    Discount coupons: Pendiente
//    Outside of plan, over KWH: STATION_ENDPOINT -> current_subscription -> remaining_kwh <= 0. Link del button: current_subscription -> account URL
//    Outside of plan, outside offpeak: STATUS_ENDPOINT -> current_subscription -> plan_covers_time == FALSE. Link del button: current_subscription -> account URL
//    Error (suspended): STATUS_ENDPOINT -> current_subscription -> status == suspended y mostrar el mensajito de issue_message y el link a account URL
//    Error (cualquier otro): STATUS_ENDPOINT -> current_subscription -> issue == TRUE y mostrar el mensajito de issue_message

}
