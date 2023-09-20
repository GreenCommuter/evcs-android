package org.evcs.android.model;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

public class Payment implements Serializable {
    public String id;
//                        "ch_processor_id": "ch_idle",
//                        "last_4": null,
    public DateTime createdAt;
    public DateTime scheduledAt;
//                        "charged": true,
//                        "declined": false,
//                        "error_message": null,
    public Integer subscriptionId;
//                        "p_type": "credit_card",
//                        "processed_by": "user",
//                        "scheduled": true,
//                        "user_name": "Nahuel Gladstein",
//                        "user_id": 7,
    public String last4;
//                        "refunded_at": null,
    public Float amount;
    public String description;
    public List<InvoiceLine> receipt;
    public Integer chargeId;

    public boolean isSubscriptionPayment() {
        return false && subscriptionId != null;
    }
}
