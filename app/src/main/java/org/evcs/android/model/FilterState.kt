package org.evcs.android.model

import java.io.Serializable
import java.util.ArrayList

class FilterState : Serializable {
    var minKw = 0
    var connectorType: ConnectorType? = null
    var onlyAvailable = false
    var authorities: ArrayList<Any>? = null

    fun isEmpty() : Boolean {
        return this == FilterState()
    }

    //This should be the default but it isn't
    override fun equals(other: Any?): Boolean {
        return other is FilterState
                && minKw == other.minKw
                && connectorType == other.connectorType
                && onlyAvailable == other.onlyAvailable
                && authorities == other.authorities
    }

}