package org.evcs.android.model

import java.io.Serializable

class FilterState : Serializable {
    var minKw = 0
    var connectorType: ConnectorType? = null
    var comingSoon: Boolean? = null
        set(value) {field = if (value!!) true else null}

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