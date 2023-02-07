package org.evcs.android.model;

import org.joda.time.DateTime;

public class SubscriptionStatus {
    public String planName;
    public boolean issue;
    public String issueMessage;
    public String accountUrl;
//            "id": 670
//                    "issue_title": null
//                    "issue_url_title": null
//                    "issue_url": null,
//                    "renewal_period": "month",
//                    "price": "49.90",
//                    "remaining_charges": null,
//                    "remaining_kwh": 12,
//                    "plan_icon_url": "https://media.evcs.com/plan.png"
    //    Kwh usage: STATUS_ENDPOINT -> current_subscription -> kwh_usage
    public int kwhUsage;
    public DateTime renewalDate;
    public boolean planCoversTime;
    public Integer remainingKwh;
    public DateTime nextRemainingKwhRestoration;

    //TODO: pero si es null el remaining puede ser unlimited
    //        "monthly_kwh":null,
    //        "weekly_kwh":null,

    public Integer getTotalKwh() {
        try {
            return remainingKwh + kwhUsage;
        } catch (Exception e) {
            return null;
        }
    }

    public String printTotalKwh() {
        return (remainingKwh == null) ? "unlimited" : getTotalKwh().toString();
    }
}
//        "plan_id":3,
//        "status":"active",
//        "active_since":"2022-10-17T13:10:16.805-07:00",
//        "plan_public":true,
//        "valid_from":"2022-10-11T13:10:00.000-07:00",
//        "valid_to":null,
//        "canceled_at":null,
//        "unlimited":true,
//        "monthly_charges":null,
//        "lockup_time":30,
//        "min_valid_charge_duration":30,
//        "referral_link":"http://subscriptions.evcs.com/refer/39",
//        "remaining_charges_restored_at":"2023-01-13T00:02:03.639-08:00",
//        "next_remaining_charge_restoration":"2023-02-13T00:02:03.639-08:00",
//        "available_locations":[],
//        "remaining_kwh_restored_at":"2023-01-13T00:02:03.639-08:00",
//        "next_remaining_kwh_restoration":"2023-02-13T00:02:03.639-08:00",
//        "start_hour":null,
//        "finish_hour":null,
//        "price_per_kwh":"0.00",
//        "on_trial_period":true,
//        "current_bc_kwh_consumed":0,
//        "plan":{
//            "id":3,
//            "name":"Unlimited Anytime",
//            "monthly_charges":null,
//            "monthly_price":"199.00",
//            "yearly_price":null,
//            "weekly_price":null,
//            "icon_url":"",
//            "short_description":"",
//            "long_description":null,
//            "start_hour":null,
//            "finish_hour":null,
//            "price_per_kwh":"0.00",
//            "monthly_kwh":null,
//            "weekly_kwh":null,
//            "trial_days":7
//        },
//        "kwh_usage":0
