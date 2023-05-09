package org.evcs.android.features.subscriptions

import org.evcs.android.model.SurveyItem
import org.evcs.android.ui.view.shared.IErrorView
import java.util.ArrayList

interface SurveyView : IErrorView {
    fun showQuestions(response: ArrayList<SurveyItem>)
    fun onSubscriptionCanceled()
}
