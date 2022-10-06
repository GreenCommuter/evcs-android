package org.evcs.android.features.auth

import org.evcs.android.ui.view.shared.IErrorView
import org.evcs.android.model.shared.RequestError

interface AuthView : IErrorView, ITermsView {
    /**
     * Callback method called by the presenter when the device token has been sent.
     */
    fun onTokenSent()
    fun showAcceptTerms()
    fun showFacebookError()
    fun showErrorPopup(error: RequestError)
}