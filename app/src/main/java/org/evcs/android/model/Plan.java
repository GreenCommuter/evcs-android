package org.evcs.android.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Plan implements Serializable {
//    public static Plan PAY_AS_YOU_GO = new Plan("0", 0, 0, "Pay as you go", 0f, 0);
//    public static Plan STANDARD_ANYTIME = new Plan("1", 0, 0, "Standard Anytime" ,49.99f, 30);
//    public static Plan UNLIMITED_OFFPEAK = new Plan("2", 20, 6, "Unlimited offpeak", 99.99f, 7);
//    public static Plan UNLIMITED_ANYTIME = new Plan("3", 0, 0, "Unlimited anytime", 199.99f, 7);
//    public static Plan BASIC_ANYTIME = new Plan("4", 0, 0, "Basic Anytime", 9.99f, 30);
//
//    public static List<Plan> values() {
//        return Arrays.asList(STANDARD_ANYTIME, BASIC_ANYTIME, PAY_AS_YOU_GO, UNLIMITED_ANYTIME, UNLIMITED_OFFPEAK);
//    }

    Plan(String id, int startHour, int finishHour, String name, float price, int trialDays) {
        this.id = id;
        this.startHour = startHour;
        this.finishHour = finishHour;
        this.name = name;
        this.monthlyPrice = price;
        this.trialDays = trialDays;
    }

    public final String id;
    public final int startHour;
    public final int finishHour;
    public final String name;
//            "monthly_charges":null,
    public Float monthlyPrice;
    public Float yearlyPrice;
    public Float weeklyPrice;
    public String iconUrl;
    public String shortDescription;
//            "long_description":null,
//            "price_per_kwh":"0.00",
//            "monthly_kwh":null,
//            "weekly_kwh":null,
    public final int trialDays;

    public RenewalPeriod getRenewalPeriod() {
        if (yearlyPrice != null) return RenewalPeriod.YEAR;
        if (monthlyPrice != null) return RenewalPeriod.MONTH;
        if (weeklyPrice != null) return RenewalPeriod.WEEK;
        return RenewalPeriod.MONTH;
    }

    public Float getPrice() {
        if (yearlyPrice != null) return yearlyPrice;
        if (monthlyPrice != null) return monthlyPrice;
        if (weeklyPrice != null) return weeklyPrice;
        return null;
    }

    public boolean isUnlimited() {
        //TODO
        return false;
    }
}
//        "pay_per_charge": false,