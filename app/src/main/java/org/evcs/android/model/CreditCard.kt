package org.evcs.android.model

import java.io.Serializable

class CreditCard : Serializable {
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
    var brand: CreditCardProvider = CreditCardProvider.UNKNOWN
    //      "checks": {
    //          "address_line1_check": null,
    //          "address_postal_code_check": null,
    //          "cvc_check": "pass"
    //      },
    //      "country": "US",
    var expL = 0
    var expYear = 0

    //      "fingerprint": "jsdakjfie",
    //      "funding": "credit",
    //      "generated_from": null,
    var last4: String? = null
}