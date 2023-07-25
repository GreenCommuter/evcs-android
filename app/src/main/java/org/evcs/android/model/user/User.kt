package org.evcs.android.model.user

import org.evcs.android.model.Subscription
import org.joda.time.DateTime
import java.io.Serializable

open class User : Serializable {

    val id = 0
    val email: String? = null
    var firstName: String? = null
        get() = field ?: ""
    var lastName: String? = null
        get() = field ?: ""
    var phone: String? = null
    var userCar: UserCar? = null
    var phoneVerifiedAt: DateTime? = null
    val zipCode: String? = null
    val ccProcessorId: String? = null
    var defaultPm: String? = null
    val name: String
        get() = "$firstName $lastName"
    val isPhoneVerified: Boolean
        get() = phoneVerifiedAt != null

    var activeSubscription: Subscription? = null
    val pendingSubscription: Subscription? = null
    val previousSubscription: Subscription? = null

    val hasAnySubscription: Boolean
        get() = activeSubscription != null || pendingSubscription != null

    val isCorporateUser: Boolean
        get() = false

    val marketingNotifications: Boolean? = null
    val hasCompletedCarScreen: Boolean
        get() = zipCode != null

//    "noodoe_id":"24",
//    "charges_last_thirty_days":null,
//    "total_charges":null,
//    "previous_subscription":null,
//    "payment_member_since":"2022-10-13T00:01:08.042-07:00",
//    "cc_type":"credit",
//    "email_confirmed_at":null,
//    "referral_link":"http://subscriptions.evcs.com/refer/22",
//    "rfid":null,
//    "lockup_waived_at":null,
//    "cc_expiration_date":"2028-11-30T23:59:59.000-08:00",
//    "cc_brand":"visa",
//    "last_sub_id":null,
//    "subscription_user":false,
//    "created_from":null,
//    "created_from_description":null,
}