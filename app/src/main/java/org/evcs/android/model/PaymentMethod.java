package org.evcs.android.model;


import java.io.Serializable;

public class PaymentMethod implements Serializable {
    public String id;
//  "object": "payment_method",
//  "billing_details": {
//      "address": {
//          "city": null,
//          "country": null,
//          "line1": null,
//          "line2": null,
//          "postal_code": null,
//          "state": null
//      },
//      "email": null,
//      "name": null,
//      "phone": null
//  },
    public CreditCard card;
//    "created": 1598907588,
//    "customer": "cus_aASDNVjf",
//    "livemode": false,
//    "metadata": {},
//    "type": "card"
}
