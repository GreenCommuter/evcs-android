package org.evcs.android.model

class StationCount {
    var dc50kw = 0
    var dc100kw = 0
    var ac = 0

    fun totalDc(): Int {
        return dc50kw + dc100kw
    }
}
