package org.evcs.android.util

import android.content.Context
import androidx.fragment.app.FragmentActivity
import org.evcs.android.BaseConfiguration
import org.evcs.android.R
import org.evcs.android.activity.WebViewActivity
import org.evcs.android.features.shared.EVCSDialogFragment
import org.evcs.android.model.shared.RequestError

object PaymentUtils {
    fun goToPendingPayment(context: Context) {
        val url = String.format(BaseConfiguration.WebViews.PAY_URL, UserUtils.getSessionToken())
        context.startActivity(WebViewActivity.buildIntent(context, "", url))
    }

    fun showPaymentDialog(context: FragmentActivity, requestError: RequestError) {
        EVCSDialogFragment.Builder()
                .setTitle("Payment")
                .setSubtitle(requestError.body)
                .withCancelOnClickListener { context.finish() }
                .addButton(context.getString(R.string.profile_payment_button)) { goToPendingPayment(context) }
                .show(context.supportFragmentManager)
    }
}