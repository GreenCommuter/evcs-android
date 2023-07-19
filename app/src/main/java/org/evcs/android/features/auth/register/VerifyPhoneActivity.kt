package org.evcs.android.features.auth.register

import org.evcs.android.activity.NavGraphActivity
import android.view.LayoutInflater
import org.evcs.android.R
import android.view.View
import org.evcs.android.databinding.ActivityBaseNavhostBinding
import org.evcs.android.util.Extras

class VerifyPhoneActivity : NavGraphActivity() {

    var mFromAuth: Boolean = false

    override fun inflate(layoutInflater: LayoutInflater): View {
        return ActivityBaseNavhostBinding.inflate(layoutInflater).root
    }

    override fun init() {
        mFromAuth = intent.getBooleanExtra(Extras.VerifyActivity.FROM_AUTH, false)
    }

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