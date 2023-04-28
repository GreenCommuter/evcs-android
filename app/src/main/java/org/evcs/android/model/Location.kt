package org.evcs.android.model

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable
import java.util.*

class Location : Serializable, ClusterItemWithText, ClusterItemWithValue {
    var id = 0
    var name: String? = null
    var latitude = 0.0
    var longitude = 0.0
    var imageUrls: List<String>? = null
    var stationCount: StationCount? = null
    var distance: Float? = null
    var stations: List<Station>? = null
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
        return stations!!.size.toString()
    }

    override fun getMarkerValue(): Int {
        return stations!!.size
    }

    val printableDistance: CharSequence
        get() = if (distance == null) "-- mi" else String.format("%.1f mi", distance)

    val connectorTypes: Set<ConnectorType>
        get() = stations!!.map { station -> station.getConnectorType() }.toSet()

    val acPrice: Float
        get() = priceOfFirstStationMatching { station ->
            station.getChargerType() == Station.ChargerType.AC }

    val dcPrice: Float
        get() = priceOfFirstStationMatching { station ->
            station.getChargerType() == Station.ChargerType.DC100KW
                    || station.getChargerType() == Station.ChargerType.DC50KW
        }

    fun priceOfFirstStationMatching(condition: (Station) -> Boolean) : Float {
        return stations!!.filter(condition).getOrNull(0)?.pricing?.detail?.priceKwh ?: 0f
    }

}