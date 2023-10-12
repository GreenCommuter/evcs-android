package org.evcs.android.model

import com.google.android.gms.maps.model.LatLng
import org.evcs.android.features.map.clustermap.ClusterItemWithDisabling
import org.evcs.android.features.map.clustermap.ClusterItemWithText
import org.evcs.android.features.map.clustermap.ClusterItemWithValue
import java.io.Serializable
import java.util.*

class Location : Serializable, ClusterItemWithText, ClusterItemWithValue, ClusterItemWithDisabling {
    var id = 0
    var name: String? = null
    var latitude = 0.0
    var longitude = 0.0
    var imageUrls: List<String>? = null
    var stationCount: StationCount? = null
    var distance: Float? = null
    var stations: List<Station>? = null
    var outlets: List<Outlet>? = null
    var address: Address? = null
    var comingSoon: Boolean? = null
    var gatecode: String? = null
    var directions: String? = null
//        get() = 1000


    //               "open_247":true,
    //               "authority":"public",
    //        "noodoe_id": "abc123",
    //        "contact": {
    //        "name": "Rina",
    //        "title": "XS",
    //        "email": "rina@sawayama.com",
    //        "phone": "1 222 333 4444",
    //        "company": "Sawayama"
    //        },
    //        "subscriptions_enabled": false
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val location = o as Location
        return id == location.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    val latLng: LatLng
        get() = LatLng(latitude, longitude)

    override fun getPosition(): LatLng {
        return latLng
    }

    override fun getMarkerText(): String {
        return stationCount!!.total().toString()
    }

    override fun getMarkerValue(): Int {
        return stationCount!!.total()
    }

    override fun isMarkerEnabled(): Boolean {
        return !(comingSoon ?: false)
    }

    override fun isClusterEnabled(other: Boolean): Boolean {
        return isMarkerEnabled() || other
    }

    val printableDistance: CharSequence
        get() = if (distance == null) "-- mi" else String.format("%.1f mi", distance)

//    val connectorTypes: Set<ConnectorType>
//        get() = stations!!.flatMap { station -> station.connectorTypes }.toSet()

    val connectorTypes: Set<ConnectorType>
        get() = outlets!!.connectorTypes()

    val acPrice: Float
        get() = priceOfFirstStationMatching { station ->
            station.getChargerType() == Station.ChargerType.AC }

    val dcPrice: Float
        get() = priceOfFirstStationMatching { station ->
            station.getChargerType() == Station.ChargerType.DC }

    fun priceOfFirstStationMatching(condition: (Station) -> Boolean) : Float {
        return stations!!.filter(condition).getOrNull(0)?.pricing?.detail?.priceKwh ?: 0f
    }

}