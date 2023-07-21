package org.evcs.android.model

import java.io.Serializable

class FilterState : Serializable {
    var minKw = 0
    var connectorTypes: MutableSet<ConnectorType> = ConnectorType.values().toMutableSet()
    var comingSoon: Boolean? = null
        set(value) {field = if (value!!) true else null}

    fun isEmpty() : Boolean {
        return this == FilterState()
    }

    fun getConnectorTypes(): Array<String> {
        return connectorTypes.map { connectorType -> connectorType.name.lowercase() }.toTypedArray()
    }

    //This should be the default but it isn't
    override fun equals(other: Any?): Boolean {
        return other is FilterState
                && minKw == other.minKw
                && connectorTypes == other.connectorTypes
                && comingSoon == other.comingSoon
    }

}