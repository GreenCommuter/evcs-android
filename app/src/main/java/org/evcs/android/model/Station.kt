package org.evcs.android.model

import androidx.annotation.AttrRes
import org.evcs.android.R
import org.evcs.android.util.StringUtils
import java.io.Serializable
import java.util.*

class Station : Serializable {
    var id = 0
    var name: String? = null
    var status: String? = null
    var kw = 0f

    //    DateTime lastSynchedAt;
    var outlets: List<Outlet>? = null
    var chargerType: String? = null
    var pricing: Pricing? = null
    var connectors: List<Connector>? = null
//    var location: Location? = null
    var locationId: Int? = null

    fun printKw(): String {
        return String.format("%.0f kW", kw)
    }

    //Maybe I could just save the tex but I did some work already with the states
    fun getAvailableStatus(): AvailableStatus {
        for (value in AvailableStatus.values()) {
            if (value.toString().equals(status, true)) {
                return value.withText(status)
            }
        }
        return AvailableStatus.OTHER.withText(status)
    }

    val isAvailable: Boolean
        get() = getAvailableStatus() == AvailableStatus.AVAILABLE

    val firstConnectorId: Int
        get() = connectors!![0].id

    enum class AvailableStatus(@AttrRes val state: Int = R.attr.state_offline) {

        AVAILABLE(R.attr.state_active), BLOCKED, OCCUPIED(R.attr.state_busy), INOPERATIVE, UNDER_REPAIR, PLANNED, REMOVED, RESERVED, UNKNOWN, OTHER;

        var text : String? = ""

        fun withText(text: String?): AvailableStatus {
            this.text = text
            return this
        }

        fun toPrintableString(): String? {
            return text
//            return StringUtils.capitalize(super.toString().lowercase(Locale.getDefault()).replace("_", " ")).toString()
        }
    }

    fun getChargerType(): ChargerType {
        for (ct in ChargerType.values()) {
            if (ct.toString().lowercase() == chargerType?.lowercase()?.substring(0, 2)) return ct
        }
        return ChargerType.AC
    }

    val connectorTypes: Set<ConnectorType>
        get() = outlets!!.connectorTypes()
//    map { outlets -> outlets.getConnectorType() }.toSet()

    fun getConnectorType(): ConnectorType {
        return outlets!![0].getConnectorType()
    }

    enum class ChargerType(val printableName: String) : Serializable {
        AC("Level 2"),
        DC("DC Fast")
    }

}