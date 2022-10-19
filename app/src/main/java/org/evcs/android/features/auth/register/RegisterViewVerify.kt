package org.evcs.android.features.auth.register

import android.content.Intent

interface RegisterViewVerify : RegisterViewCellphone {
    fun onCellphoneVerified()
    fun showCode(s: String?)
    fun openConsentDialog(consentIntent: Intent);
}
