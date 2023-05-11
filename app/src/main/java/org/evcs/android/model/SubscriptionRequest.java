package org.evcs.android.model;

import java.util.ArrayList;

public class SubscriptionRequest {
    public String planId;
    public RenewalPeriod renewalPeriod;
    public String paymentMethod;
    public ArrayList<String> couponCodes;
    public String referrerNodooeId;
    public CreatedFrom createdFrom;
    public String createdFromDescription;

    private enum CreatedFrom { HERTZ, ADS, NOODOE }

    public SubscriptionRequest(String planId, String paymentMethod) {
        this.planId = planId;
        this.renewalPeriod = RenewalPeriod.MONTH;
        this.couponCodes = new ArrayList<>();
        this.paymentMethod = paymentMethod;
    }
}
