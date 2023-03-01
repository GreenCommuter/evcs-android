package org.evcs.android.model.user

import org.joda.time.DateTime
import java.io.Serializable

open class User : Serializable {
    val id = 0
    val email: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var phone: String? = null
    var userCar: UserCar? = null
    private val phoneVerifiedAt: DateTime? = null
    val zipCode: String? = null
    val ccProcessorId: String? = null
    var defaultPm: String? = null
    val name: String
        get() = "$firstName $lastName"
    val isPhoneVerified: Boolean
        get() = phoneVerifiedAt != null
}