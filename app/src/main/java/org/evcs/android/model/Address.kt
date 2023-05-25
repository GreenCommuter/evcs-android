package org.evcs.android.model

import java.io.Serializable

class Address : Serializable {

    var streetAddress: String? = null
    var city: String? = null
    var state: String? = null
    var zipCode: String? = null
    var country: String? = null

    override fun toString(): String {
        return "$streetAddress,\n$city, $state $zipCode"
    }
}