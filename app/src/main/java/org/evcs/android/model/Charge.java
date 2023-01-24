package org.evcs.android.model;

import org.joda.time.DateTime;

import java.io.Serializable;

public class Charge implements Serializable {
    //        "id": 3,
    private String noodoeId;
    private DateTime startedAt;
//            "completed_at": "2020-08-13T15:01:51.000Z",
    private int locationId;
    private int duration; //unit? ms?

//            "charger_type": "DC",
//            "connector": "CHAdeMO",
    private float kwh;
    private float price;

//            "user_id": 1,
//            "user_name": "Osvaldo",
//            "subscription": {
//        "id": 1,
//                "issue": false,
//                "issue_message": null,
//                "issue_url": null,
//                "issue_url_title": null,
//                "issue_title": null,
//                "referral_link": "https://dev.evcs.com/refer/abc123",
//                "renewal_period": "month",
//                "price": 49.99,
//                "remaining_charges": 8,
//                "remaining_kwh": 50,
//                "available_locations": [],
//        "account_url": "https://example.com",
//                "unlimited": false,
//                "plan_id": 2,
//                "active_since": "2020-08-28T14:36:54Z",
//                "renewal_date": "2020-09-28T14:36:54Z",
//                "status": "active"
//        "valid_from": null,
//                "valid_to": "2020-09-28T14:36:54Z",
//                "lockup_time": 10,
//                "min_valid_charge_duration": 5,
//                "monthly_charges": 10,
//                "monthly_kwh": 10,
//                "plan": {
//            "id": 1,
//                    "name": "Basic",
//                    "monthly_charges": 10,
//                    "monthly_price": 49.99,
//                    "yearly_price": 499.99,
//                    "icon_url": "htpps://example.com/plan.png",
//                    "short_description": "short desc",
//                    "long_description": "long desc",
//                    "start_hour": 8,
//                    "finish_hour": 17,
//                    "pay_per_charge": false,
//                    "price_per_kwh": 9,
//                    "monthly_kwh": 100,
//                    "trial_days": 7
//
//        }
//    }
//    }

    public DateTime getStartedAt() {
        return startedAt;
    }

    public float getPrice() {
        return price;
    }

    public int getLocationId() {
        return locationId;
    }

    public int getDuration() {
        return duration;
    }

    public float getKwh() {
        return kwh;
    }

}
