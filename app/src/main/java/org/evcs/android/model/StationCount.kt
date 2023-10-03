package org.evcs.android.model

import java.io.Serializable

class StationCount : Serializable {
    var dc = 0
    var ac = 0

    fun total(): Int {
        return dc + ac
    }
}
