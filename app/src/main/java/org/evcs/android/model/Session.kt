package org.evcs.android.model

import org.joda.time.DateTime
import java.io.Serializable

class Session : Charge(), Serializable {
    //duration
    //    "kwh":"0.0",
    //    "status":"active",
    val updatedAt: DateTime? = null
    val ongoingRate: OngoingRate? = null
    val firstSoc: Int? = null
    val lastSoc: Int? = null
}