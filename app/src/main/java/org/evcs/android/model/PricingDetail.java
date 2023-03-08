package org.evcs.android.model;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class PricingDetail implements Serializable {
    public Float priceKwh;
    private Float initialFee;
    private Float occupancyFlatFee;
    private Integer bufferTime;
//    int firstHour;
//    int firstHourPrice;
    public Integer thereafterPrice;
//    int regularMinimumCost;
    public boolean showFreeChargingCode;
    public String freeChargingCode;

    //TODO: chequear que pasa si es null
    public String printPriceKwh() {
        return String.format("%.2f USD/kWh", priceKwh);
    }

    public @Nullable String printConnectionFee() {
        if (initialFee == null) return null;
        if (initialFee == 0f) return "Free";
        return initialFee.toString();
    }

    public @Nullable String printOccupancyFee() {
        if (occupancyFlatFee == null) return null;
        return String.format("%.2f USD/charge", occupancyFlatFee);
    }

    public @Nullable String printBufferTime() {
        if (bufferTime == null) return null;
        return String.format("%d mins", bufferTime);
    }
}
