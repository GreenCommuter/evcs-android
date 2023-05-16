package org.evcs.android.features.profile.wallet

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.evcs.android.R
import org.evcs.android.activity.NavGraphActivity
import org.evcs.android.databinding.ActivityBaseNavhostBinding
import org.evcs.android.model.PaymentMethod
import org.evcs.android.ui.view.shared.PaymentMethodView
import org.evcs.android.util.Extras

class WalletActivity : NavGraphActivity() {

    var mFinishOnClick = false

    override fun inflate(layoutInflater: LayoutInflater): View {
        return ActivityBaseNavhostBinding.inflate(layoutInflater).root
    }

    override fun init() {
        mFinishOnClick = intent.getBooleanExtra(Extras.ChangePaymentMethod.FINISH_ON_CLICK, false)
    }

    override fun getNavGraphId(): Int {
        return R.navigation.navigation_wallet
    }

    private fun finishWithResult(pm: PaymentMethod) {
        val data = Intent()
        data.putExtra(Extras.ChangePaymentMethod.PAYMENT_METHODS, pm)
        setResult(RESULT_OK, data)
        finish()
    }

    fun onPaymentMethodChanged(pm: PaymentMethod) {
        if (mFinishOnClick) finishWithResult(pm)
    }

    companion object {
        fun buildIntent(context: Context?, finishOnClick: Boolean): Intent {
            val intent = Intent(context, WalletActivity::class.java)
            intent.putExtra(Extras.ChangePaymentMethod.FINISH_ON_CLICK, finishOnClick)
            return intent
        }

        fun getDefaultLauncher(activity: ComponentActivity, paymentMethodView: PaymentMethodView): ActivityResultLauncher<Intent> {
            return activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result -> if (result.resultCode == RESULT_OK) {
                    val pm = result.data!!.getSerializableExtra(Extras.ChangePaymentMethod.PAYMENT_METHODS)
                    paymentMethodView.setPaymentMethod(pm as PaymentMethod)
                }
            }
        }
    }
}