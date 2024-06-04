package org.evcs.android.model;

import com.google.gson.annotations.SerializedName;

import org.evcs.android.EVCSApplication;
import org.evcs.android.R;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.io.Serializable;
import java.util.ArrayList;

public class Charge implements Serializable {
    private int id;
    private String noodoeId;
    private String startedAt;
    private String completedAt;
    private int locationId;
    private String locationName;
    private float duration; //unit? ms?

//            "charger_type": "DC",
//            "connector": "CHAdeMO",
    private float kwh;
//    private float price;
    private String status;
    private String stationName;
    private Subscription subscription;
    private Float ppkwh;
    private Location location;
    private CreditCardProvider paymentBrand;
    private Float paymentAmount;
    @SerializedName("payment_last_4")
    private String paymentLast4;
    public ArrayList<InvoiceLine> costBreakdown;

    public int getId() {
        return id;
    }

    public DateTime getStartedAt() {
        return DateTime.parse(startedAt);
    }

    public DateTime getCompletedAt() {
        return DateTime.parse(completedAt);
    }

    public String getLocationName() {
        return locationName != null ? locationName : location.getName();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public float getDuration() {
        return duration * 1000;
    }

    public String getPrintableDuration() {
        if (getDuration() == 0) return "--";
        Period period = new Period(0, (long) getDuration());
        PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
                .appendHours().appendSuffix(" hr, ").printZeroAlways()
                .appendMinutes().appendSuffix(" min, ")
                .appendSeconds().appendSuffix(" sec")
                .toFormatter();
        return periodFormatter.print(period);
    }

    public String printKwh() {
        return String.format("%.4f kWh", kwh);
    }

    public String getStatus() {
        return status;
    }

    public boolean isCharging() {
        return status.equalsIgnoreCase("active");
    }

    public String getPlanName() {
        if (subscription == null || subscription.planName == null)
            return EVCSApplication.getInstance().getApplicationContext()
                .getString(R.string.pay_as_you_go_name);
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

    public CreditCardProvider getPaymentBrand() {
        return paymentBrand;
    }

    public Float getPaymentAmount() {
        return paymentAmount;
    }

    public String getPaymentLast4() {
        return paymentLast4;
    }

    public Station getStation() {
        for (Station s : location.getStations()) {
            if (s.getName().equals(stationName)) return s;
        }
        return null;
    }
}
