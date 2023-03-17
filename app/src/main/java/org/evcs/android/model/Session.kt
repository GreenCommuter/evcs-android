package org.evcs.android.model

import org.joda.time.DateTime
import java.io.Serializable

class Session : Charge(), Serializable {
    //duration
    //    "kwh":"0.0",
    //    "status":"active",
    val updatedAt: DateTime? = null
    val stationName = 0

    override fun getDuration(): Float {
        return super.getDuration() * 60
    }
}