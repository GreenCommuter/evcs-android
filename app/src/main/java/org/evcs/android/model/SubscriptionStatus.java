package org.evcs.android.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.evcs.android.util.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.io.Serializable;

public class SubscriptionStatus implements Serializable {
    public String id;
    public String planName;
    public boolean issue;
    public String issueMessage;
    public String accountUrl;
//                    "issue_title": null
//                    "issue_url_title": null
//                    "issue_url": null,
    public String renewalPeriod;
    public float price;
//                    "plan_icon_url": "https://media.evcs.com/plan.png"
    //    Kwh usage: STATUS_ENDPOINT -> current_subscription -> kwh_usage
    public Status status;
    public int kwhUsage;
    public DateTime renewalDate;
    public boolean planCoversTime;
    public Integer remainingKwh;
    public DateTime nextRemainingKwhRestoration;
    public Plan plan;
    public boolean onTrialPeriod;

    //        "issue_url": null,
//        "issue_url_title": null,
//        "issue_title": null,
//        "referral_link": "https://dev.evcs.com/refer/abc123",
//        "remaining_charges": 8,
    public boolean unlimited;
//        "plan_id": 2,
    public DateTime activeSince;
    //        "valid_from": null,
    public DateTime validTo;
//        "lockup_time": 10,
//        "min_valid_charge_duration": 5,
//        "price_per_kwh": 9,
    //        "plan_start_hour":0,
//        "plan_finish_hour":0,
//        "plan_public":true,
//        "canceled_at":"2022-09-27T13:44:09.207-07:00",
//        "created_from":null,
//        "created_from_description":null,
//        "unlimited":true,
//        "plan_icon_url":"http://admin.evcs.com/media/60d231925328c_md.png",
//        "available_locations":[],
//        "remaining_kwh_restored_at":"2023-04-13T00:01:07.717-07:00",
//        "start_hour":0,
//        "finish_hour":0,
//        "current_bc_kwh_consumed":0,
//        "monthly_charges":"200.0",
//        "remaining_charges_restored_at":"2023-04-13T00:01:07.717-07:00",
//        "next_remaining_charge_restoration":"2023-05-13T00:01:07.717-07:00",

    //si es null el remaining puede ser unlimited
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
        return (unlimited) ? "unlimited" : getTotalKwh().toString();
    }

    public boolean isSuspended() {
        return status.equals(Status.SUSPENDED);
    }

    public enum Status { @SerializedName("active") ACTIVE, @SerializedName("suspended") SUSPENDED, @SerializedName("canceled") CANCELED;
        @NonNull
        @Override
        public String toString() {
            return StringUtils.capitalize(super.toString().toLowerCase()).toString();
        }
    }

    public int getActiveDaysLeft() {
        return (int) new Duration(new DateTime(), renewalDate).getStandardDays();
    }

    public boolean isCanceled() {
        return validTo != null;
    }
}
