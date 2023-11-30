package org.evcs.android.model

import android.text.TextUtils

class SubscriptionSurveyRequest {
    var subscriptionId: String? = null
    var items: ArrayList<SurveyAnswer> = ArrayList()

    constructor(subscriptionId: String, checkedItems: HashSet<String>, otherId: String, text: String) {
        this.subscriptionId = subscriptionId
        items.addAll(checkedItems.map { item -> SurveyAnswer(item) })
        if (!TextUtils.isEmpty(text))
            items.add(SurveyAnswer(otherId, text))
    }
}