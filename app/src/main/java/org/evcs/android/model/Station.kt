package org.evcs.android.model

import androidx.annotation.AttrRes
import org.evcs.android.R
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
    var location: Location? = null

    fun printKw(): String {
        return String.format("%.0f kW", kw)
    }

    fun getAvailableStatus(): AvailableStatus {
        for (value in AvailableStatus.values()) {
            if (availableStatus == value.toString()) {
                return value
            }
        }
        return AvailableStatus.UNKNOWN
    }

    val isAvailable: Boolean
        get() = getAvailableStatus() == AvailableStatus.AVAILABLE

    val firstConnectorId: Int
        get() = connectors!![0].id

    enum class AvailableStatus(@AttrRes val state: Int = R.attr.state_offline) {
        AVAILABLE(R.attr.state_active), BLOCKED, IN_USE(R.attr.state_busy), INOPERATIVE, UNDER_REPAIR, PLANNED, REMOVED, RESERVED, UNKNOWN;

        fun toPrintableString(): String {
            return StringUtils.capitalize(super.toString().lowercase(Locale.getDefault()).replace("_", " ")).toString()
        }
    }

    fun getChargerType(): ChargerType {
        for (ct in ChargerType.values()) {
            if (ct.toString().lowercase() == chargerType?.lowercase()) return ct
        }
        return ChargerType.AC
    }

    val connectorTypes: Set<ConnectorType>
        get() = outlets!!.map { outlets -> outlets.getConnectorType() }.toSet()

    fun getConnectorType(): ConnectorType {
        return outlets!![0].getConnectorType()
    }

    enum class ChargerType(val printableName: String) : Serializable {
        AC("Level 2"),
        DC50KW("DC Fast"),
        DC100KW("DC Fast")
    }

}