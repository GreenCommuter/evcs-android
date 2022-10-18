package org.evcs.android.features.auth.register

interface RegisterViewVerify : RegisterViewCellphone {
    fun onCellphoneVerified()
    fun showCode(s: String?)
}
