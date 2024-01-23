package org.evcs.android.model;

import org.evcs.android.model.user.TextWrapper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;
import java.util.ArrayList;

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
    public Integer startHour;
    public Integer finishHour;
    public final String name;
//            "monthly_charges":null,
    private Float monthlyPrice;
    private Float yearlyPrice;
    private Float weeklyPrice;
    public String iconUrl;
    public String shortDescription;
//            "long_description":null,
    public Float pricePerKwh;
    public Float monthlyKwh;
    public Float weeklyKwh;
    public int trialDays;
    public int trialKwh;
    public String cta;
//    public String tab;
    public String banner;
    public String useCase;
    public String displayBanner;
    public String displayTopText;
    public String displaySubtitle;
    public String displayUnderSubtitle;
    public String priceTitle;
    public ArrayList<String> displayDetails;
    public ArrayList<TextWrapper> displayButtons;

    public RenewalPeriod getRenewalPeriod() {
        if (weeklyPrice != null) return RenewalPeriod.WEEK;
        if (monthlyPrice != null) return RenewalPeriod.MONTH;
        if (yearlyPrice != null) return RenewalPeriod.YEAR;
        return RenewalPeriod.MONTH;
    }

    public Float getPrice() {
        if (weeklyPrice != null) return weeklyPrice;
        if (monthlyPrice != null) return monthlyPrice;
        if (yearlyPrice != null) return yearlyPrice;
        return null;
    }

    public boolean isUnlimited() {
        return kwhCap() == null;
    }

    public boolean isTimeLimited() {
        if (startHour == null) return false;
        return (startHour != 0 || finishHour != 0);
    }

    public Integer kwhCap() {
        if (monthlyKwh != null) return monthlyKwh.intValue();
        if (weeklyKwh != null) return weeklyKwh.intValue();
        return null;
    }

    private static String intToHour(Integer startHour) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("ha");
        return formatter.print(new DateTime().withHourOfDay(startHour));
    }

    public String startHour() {
        return intToHour(startHour);
    }

    public String finishHour() {
        return intToHour(finishHour);
    }

    public Integer milesCap() {
        if (kwhCap() == null) return null;
        return (int) (kwhCap() * 3.8f);
    }

    public DateTime startingDate() {
        return DateTime.now().plusDays(trialDays);
    }
}
//        "pay_per_charge": false,