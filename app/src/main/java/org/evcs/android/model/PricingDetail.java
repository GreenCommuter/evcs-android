package org.evcs.android.model;

import java.io.Serializable;

public class PricingDetail implements Serializable {
    float priceKwh;
    float initialFee;
//            "occupancy_flat_fee": null,
//            "buffer_time": null
    int firstHout;
    int firstHourPrice;
    int thereafterPrice;
    int regularMinimumCost;

    public float getPriceKwh() {
        return priceKwh;
    }
}
