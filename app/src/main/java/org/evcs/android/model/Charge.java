package org.evcs.android.model;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.io.Serializable;

public class Charge implements Serializable {
    private int id;
    private String noodoeId;
    private DateTime startedAt;
//            "completed_at": "2020-08-13T15:01:51.000Z",
    private int locationId;
    private String locationName;
    private float duration; //unit? ms?

//            "charger_type": "DC",
//            "connector": "CHAdeMO",
    private float kwh;
    private float price;
    private String status;
    private Subscription subscription;

    public int getId() {
        return id;
    }

    public DateTime getStartedAt() {
        return startedAt;
    }

    public float getPrice() {
        return price;
    }

    public int getLocationId() {
        return locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public float getDuration() {
        return duration * 1000;
    }

    public String getPrintableDuration() {
        if (getDuration() == 0) return "--";
        Period period = new Period(0, (long) getDuration());
        PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
                .appendHours().appendSuffix("hr ")
                .appendMinutes().appendSuffix("min")
                .toFormatter();
        return periodFormatter.print(period);
    }

    public float getKwh() {
        return kwh;
    }

    public String getStatus() {
        return status;
    }

    public boolean isCharging() {
        return status.equalsIgnoreCase("active");
    }

    public String getPlanName() {
        return subscription.planName;
    }
}
