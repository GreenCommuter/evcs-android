package org.evcs.android.features.auth.register

import org.evcs.android.activity.NavGraphActivity
import android.view.LayoutInflater
import org.evcs.android.R
import android.app.Activity
import android.view.View
import org.evcs.android.databinding.ActivityBaseNavhostBinding

class VerifyPhoneActivity : NavGraphActivity() {

    override fun inflate(layoutInflater: LayoutInflater): View {
        return ActivityBaseNavhostBinding.inflate(layoutInflater).root
    }

    override fun init() {}

    override fun getNavGraphId(): Int {
        return R.navigation.navigation_verify
    }

    fun onVerifyFinished() {
        setResult(RESULT_OK)
        finish()
    }

    fun onCancel() {
        setResult(RESULT_CANCELED)
        finish()
    }
}