package org.evcs.android.model

import java.io.Serializable

class FilterState : Serializable {
    var minKw = 0
    var connectorType: ConnectorType? = null
    var comingSoon = false

    fun isEmpty() : Boolean {
        return this == FilterState()
    }

    //This should be the default but it isn't
    override fun equals(other: Any?): Boolean {
        return other is FilterState
                && minKw == other.minKw
                && connectorType == other.connectorType
                && comingSoon == other.comingSoon
    }

}