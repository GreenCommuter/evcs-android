package org.evcs.android.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.Navigation.findNavController
import org.evcs.android.R
import org.evcs.android.databinding.ActivityBaseNavhostBinding
import org.evcs.android.features.charging.ChargingNavigationController
import org.evcs.android.model.Session
import org.evcs.android.util.Extras

class ChargingActivity : NavGraphActivity() {

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

    override fun init() {}

    override fun getNavGraphId(): Int {
        return R.navigation.charging_graph
    }
}