package org.evcs.android.model;

import org.joda.time.DateTime;

public class SubscriptionStatus {
    public String id;
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
//                    "plan_icon_url": "https://media.evcs.com/plan.png"
    //    Kwh usage: STATUS_ENDPOINT -> current_subscription -> kwh_usage
    public String status;
    public int kwhUsage;
    public DateTime renewalDate;
    public boolean planCoversTime;
    public Integer remainingKwh;
    public DateTime nextRemainingKwhRestoration;
    public Plan plan;
    //on_trial_period

    //        "issue_url": null,
//        "issue_url_title": null,
//        "issue_title": null,
//        "referral_link": "https://dev.evcs.com/refer/abc123",
//        "renewal_period": "month",
//        "price": 49.99,
//        "remaining_charges": 8,
//        "unlimited": false,
//        "plan_id": 2,
    public DateTime activeSince;
    //        "valid_from": null,
    public DateTime validTo;
//        "lockup_time": 10,
//        "min_valid_charge_duration": 5,
//        "monthly_kwh": 10,
//        "weekly_kwh": null,
//        "price_per_kwh": 9,

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

    public boolean isSuspended() {
        return "suspended".equals(status);
    }
}
