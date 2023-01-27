package org.evcs.android.model;

import java.io.Serializable;

public class CreditCard implements Serializable {
    private CreditCardProvider brand;
//      "checks": {
//          "address_line1_check": null,
//          "address_postal_code_check": null,
//          "cvc_check": "pass"
//      },
//      "country": "US",
    public int expL;
    public int expYear;
//      "fingerprint": "jsdakjfie",
//      "funding": "credit",
//      "generated_from": null,
    public String last4;
//      "networks": {
//          "available": [
//              "visa"
//          ],
//          "preferred": null
//      },
//      "three_d_secure_usage": {
//          "supported": true
//      },
//      "wallet": null

    public CreditCardProvider getProvider() {
        return brand;
    }
}
