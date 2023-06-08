package org.evcs.android.features.profile.plans

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.evcs.android.R
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.databinding.ActivityBaseBinding
import org.evcs.android.features.auth.register.VerifyPhoneActivity
import org.evcs.android.model.Plan
import org.evcs.android.model.SubscriptionStatus
import org.evcs.android.util.Extras
import org.evcs.android.util.UserUtils

class GetPlanActivity : BaseActivity2() {

    private lateinit var mLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        mLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result -> if (result.resultCode != RESULT_OK) finish()
        }
        super.onCreate(savedInstanceState)
    }

    override fun inflate(layoutInflater: LayoutInflater): View {
        return ActivityBaseBinding.inflate(layoutInflater).root
    }

    override fun init() {
        if (!(UserUtils.getLoggedUser().isPhoneVerified) || UserUtils.getLoggedUser().userCar == null) {
            mLauncher.launch(Intent(this, VerifyPhoneActivity::class.java))
        }
    }

    override fun populate() {
        val plan = intent.getSerializableExtra(Extras.PlanActivity.PLAN) as Plan
        val fragment = if (intent.getBooleanExtra(Extras.PlanActivity.HAS_PLAN, false))
            SwitchPlanFragment.newInstance(plan)
        else GetPlanFragment.newInstance(plan)
        replaceFragment(R.id.activity_base_content, fragment)
    }

    fun onPlanSubscribed(subscription: SubscriptionStatus) {
        val fragment = PlanStartedFragment.newInstance(subscription.plan)
        replaceFragment(R.id.activity_base_content, fragment)
        setResult(RESULT_OK)
    }

}
