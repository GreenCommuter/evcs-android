package org.evcs.android.model

import java.io.Serializable

class Outlet : Serializable {
    var type: String? = null
    var count = 0

    fun getConnectorType(): ConnectorType {
        for (ct in ConnectorType.values()) {
            if (ct.toString().lowercase() == type?.lowercase()) return ct
        }
        return ConnectorType.TYPE1
    }

}
fun List<Outlet>.connectorTypes() : Set<ConnectorType> {
    return this.map { outlet -> outlet.getConnectorType() }.toSet()
}