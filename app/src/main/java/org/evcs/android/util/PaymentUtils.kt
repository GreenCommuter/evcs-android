package org.evcs.android.util

import android.content.Context
import android.view.Gravity
import androidx.fragment.app.FragmentActivity
import com.base.core.util.NavigationUtils
import org.evcs.android.R
import org.evcs.android.activity.ChargingActivity
import org.evcs.android.activity.PayBalanceActivity
import org.evcs.android.features.profile.wallet.WalletActivity
import org.evcs.android.features.shared.EVCSDialogFragment
import org.evcs.android.model.shared.RequestError

object PaymentUtils {
    fun goToPendingPayment(context: Context) {
        NavigationUtils.jumpTo(context, PayBalanceActivity::class.java)
    }

    fun showPaymentDialog(context: FragmentActivity, requestError: RequestError) {
        EVCSDialogFragment.Builder()
                .setTitle("Payment")
                .setSubtitle(requestError.body)
                .withCancelOnClickListener { context.finish() }
                .addButton(context.getString(R.string.profile_payment_button)) { goToPendingPayment(context) }
                .show(context.supportFragmentManager)
    }

    fun showPreauthDialog(context: FragmentActivity, retryCallback: () -> Unit) {
        EVCSDialogFragment.Builder()
            .setTitle("Payment")
            .setSubtitle(context.getString(R.string.plan_info_payment_error_subtitle), Gravity.CENTER)
            .addButton(context.getString(R.string.start_charging_error_retry), {
                fragment -> fragment.dismiss()
                retryCallback.invoke()
            }, R.style.ButtonK_Blue)
            .addButton(context.getString(R.string.plan_info_payment_error_update), {
                context.setResult(ChargingActivity.RESULT_CANCELED_WITH_DIALOG)
                context.finish()
                context.startActivity(WalletActivity.buildIntent(context, true))
            }, R.style.ButtonK_BlueOutline)
            .withCancelOnClickListener { context.finish() }
            .show(context.supportFragmentManager)
    }
}