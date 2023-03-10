package org.evcs.android.model

import org.evcs.android.util.StringUtils
import java.io.Serializable
import java.util.*

class Station : Serializable {
    var id = 0
    var name: String? = null
    var availableStatus: String? = null
    var kw = 0f

    //    DateTime lastSynchedAt;
    var outlets: List<Outlet>? = null
    var chargerType: String? = null
    var pricing: Pricing? = null
    var connectors: List<Connector>? = null

    fun printKw(): String {
        return String.format("%.0fkW", kw)
    }

    fun getChargerType(): ConnectorType {
        for (ct in ConnectorType.values()) {
            if (ct.toString() == chargerType) return ct
        }
        return ConnectorType.TYPE1
    }

    fun getAvailableStatus(): AvailableStatus {
        for (value in AvailableStatus.values()) {
            if (availableStatus == value.toString()) {
                return value
            }
        }
        return AvailableStatus.UNKNOWN
    }

    fun printAvailableStatus(): CharSequence {
        return StringUtils.capitalize(availableStatus!!.lowercase(Locale.getDefault()).replace("_", " "))
    }

    val isAvailable: Boolean
        get() = getAvailableStatus() == AvailableStatus.AVAILABLE

    val firstConnectorId: Int
        get() = connectors!![0].id

    enum class AvailableStatus {
        AVAILABLE, BLOCKED, IN_USE, INOPERATIVE, UNDER_REPAIR, PLANNED, REMOVED, RESERVED, UNKNOWN
    }

}