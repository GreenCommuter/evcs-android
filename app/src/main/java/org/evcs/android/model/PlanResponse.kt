package org.evcs.android.model

import java.io.Serializable

class PlanResponse : Serializable {
    var tabs : ArrayList<PlanTab>? = null
}

class PlanTab : Serializable {
    var title: String? = null
    var plans: ArrayList<Plan>? = null
}