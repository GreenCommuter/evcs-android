package org.evcs.android.model

import java.io.Serializable

class OngoingRate : Serializable {
    var rateLabel: String? = null
    //Should be float
    var rateValue: String? = null
    var optionalExplanation: String? = null
    var sessionFeeLabel: String? = null
    //Should be float
    var sessionFeeValue: String? = null
}