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
    private String stationName;
    private Subscription subscription;
    private Float ppkwh;
    private Location location;
    private CreditCardProvider paymentBrand;

    public int getId() {
        return id;
    }

    public DateTime getStartedAt() {
        return startedAt;
    }

    public float getPrice() {
        return price;
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

    public String printKwh() {
        return String.format("%.3f kW", kwh);
    }

    public String getStatus() {
        return status;
    }

    public boolean isCharging() {
        return status.equalsIgnoreCase("active");
    }

    public String getPlanName() {
        if (subscription == null) return null;
        return subscription.planName;
    }

    public String getStationName() {
        return stationName;
    }

    public Float getPpkwh() {
        return ppkwh;
    }

    public String getImage() {
        if (location == null) return null;
        if (location.getImageUrls() != null && location.getImageUrls().size() > 0) {
            return location.getImageUrls().get(0);
        }
        return null;
    }

    public String getAddress() {
        if (location == null) return null;
        return location.getAddress().toString();
    }
}
