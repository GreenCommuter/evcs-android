package org.evcs.android.model

import java.io.Serializable

class StationCount : Serializable {
    var dc50kw = 0
    var dc100kw = 0
    var ac = 0

    fun totalDc(): Int {
        return dc50kw + dc100kw
    }
}
