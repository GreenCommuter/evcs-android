package org.evcs.android.model;

import org.joda.time.DateTime;

import java.util.List;

public class Payment {
//                "id": 12,
//                        "ch_processor_id": "ch_idle",
//                        "last_4": null,
    public DateTime createdAt;
//                        "scheduled_at": "2021-07-28T00:00:00.000Z",
//                        "charged": true,
//                        "declined": false,
//                        "error_message": null,
//                        "subscription_id": 3,
//                        "p_type": "credit_card",
//                        "processed_by": "user",
//                        "scheduled": true,
//                        "user_name": "Nahuel Gladstein",
//                        "user_id": 7,
//                        "last_4": 1111,
//                        "refunded_at": null,
    public Float amount;
    List<InvoiceLine> invoiceLines;

}
