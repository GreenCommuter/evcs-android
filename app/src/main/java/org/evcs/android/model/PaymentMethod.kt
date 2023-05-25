package org.evcs.android.model

import com.base.networking.retrofit.serializer.BaseGsonBuilder
import org.evcs.android.util.Extras
import org.evcs.android.util.StorageUtils
import java.io.Serializable

class PaymentMethod : Serializable {

    var id: String? = null

    //  "object": "payment_method",
    var billingDetails: BillingDetails? = null
    lateinit var card: CreditCard
    //    "created": 1598907588,
    //    "customer": "cus_aASDNVjf",
    //    "livemode": false,
    //    "metadata": {},
    //    "type": "card"

    companion object {
        fun getDefaultFromSharedPrefs(): PaymentMethod? {
            val json = StorageUtils.getStringFromSharedPreferences(Extras.ChangePaymentMethod.PAYMENT_METHODS, "")
            val gson = BaseGsonBuilder.getBaseGsonBuilder().create()
            return gson.fromJson(json, PaymentMethod::class.java)
        }
    }
}