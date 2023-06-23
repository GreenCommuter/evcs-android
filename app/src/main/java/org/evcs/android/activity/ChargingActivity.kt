package org.evcs.android.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation.findNavController
import org.evcs.android.R
import org.evcs.android.databinding.ActivityBaseNavhostBinding
import org.evcs.android.features.charging.ChargingNavigationController
import org.evcs.android.features.shared.EVCSDialogFragment
import org.evcs.android.model.Session
import org.evcs.android.util.Extras

class ChargingActivity : NavGraphActivity() {

    private var mIsActiveSession = false

    private lateinit var mNavigationController: ChargingNavigationController

    override fun inflate(layoutInflater: LayoutInflater): View {
        return ActivityBaseNavhostBinding.inflate(layoutInflater).root
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mNavigationController = ChargingNavigationController(findNavController(this, R.id.activity_base_content))
        if (intent.hasExtra(Extras.StartCharging.SESSION)) {
            mNavigationController.onChargingStarted(intent.getSerializableExtra(Extras.StartCharging.SESSION) as Session)
        } else {
            mNavigationController.goToPlanInfo(intent.getStringExtra(Extras.PlanInfo.STATION_ID))
        }
    }

    fun cancelSession(fm: FragmentManager?) {
        cancelSession(fm) { finish() }
    }

    fun cancelSession(fm: FragmentManager?, callback: () -> Unit) {
        if (mIsActiveSession) {
            EVCSDialogFragment.Builder()
                    .setTitle(getString(R.string.plan_info_dialog_cancel))
                    .addButton(getString(R.string.app_yes)) { fragment ->
                        mIsActiveSession = false
                        fragment.dismiss()
                        callback.invoke()
                    }
                    .showCancel(true)
                    .show(fm!!)
        } else {
            callback.invoke()
        }
    }

    fun setActiveSession() {
        mIsActiveSession = true
    }

    override fun init() {}

    override fun getNavGraphId(): Int {
        return R.navigation.charging_graph
    }
}