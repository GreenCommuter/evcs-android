package org.evcs.android.features.profile.wallet

import android.os.Bundle
import android.util.Log
import android.view.View
import com.stripe.android.PaymentConfiguration
import com.stripe.android.googlepaylauncher.GooglePayPaymentMethodLauncher
import org.evcs.android.Configuration
import org.evcs.android.features.profile.wallet.PaymentMethodAdapterV2.CreditCardListener
import org.evcs.android.model.PaymentMethod
import org.evcs.android.util.UserUtils

class ChangeWalletHeaderFragment : WalletHeaderFragment() {

    private lateinit var mGooglePayLauncher: GooglePayPaymentMethodLauncher

    companion object {
        fun newInstance(): ChangeWalletHeaderFragment {
            val args = Bundle()
            val fragment = ChangeWalletHeaderFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun populate() {
        super.populate()
        setUpGpay()
        mGpay.visibility = View.VISIBLE
        mGpay.setGooglePay()
    }

    override fun setListeners() {
        super.setListeners()
        mGpay.setOnChangeClickListener {
            mGooglePayLauncher.present(
                    currencyCode = "USD",
            )
        }
    }

    fun setUpGpay() {
        PaymentConfiguration.init(requireContext(), Configuration.STRIPE_KEY)

        mGooglePayLauncher = GooglePayPaymentMethodLauncher(
                fragment = this,
                config = GooglePayPaymentMethodLauncher.Config(
                        environment = Configuration.GOOGLE_PAY_ENV,
                        merchantCountryCode = "US",
                        merchantName = "EVCS"
                ),
                readyCallback = ::onGooglePayReady,
                resultCallback = ::onGooglePayResult
        )

    }

    private fun onGooglePayReady(isReady: Boolean) {
        Log.d("Google pay ready", isReady.toString())
//        mBinding.planInfoGpay.isEnabled = isReady
    }

    private fun onGooglePayResult(result: GooglePayPaymentMethodLauncher.Result) {
        when (result) {
            is GooglePayPaymentMethodLauncher.Result.Completed -> {
                (activity as WalletActivity).onPaymentMethodChanged(PaymentMethod.GPay(result.paymentMethod.id))
            }

            GooglePayPaymentMethodLauncher.Result.Canceled -> {
                // User canceled the operation
            }
            is GooglePayPaymentMethodLauncher.Result.Failed -> {
                // Operation failed; inspect `result.error` for the exception
            }
        }
    }

    override fun getOnItemClickListener(): CreditCardListener {
        return object : CreditCardListener {
            override fun onDetailClicked(item: PaymentMethod) {}

            override fun onStarClicked(item: PaymentMethod) {
                if (item.id != UserUtils.getLoggedUser().defaultPm) {
                    showProgressDialog()
                    presenter.makeDefaultPaymentMethod(item)
                }
            }
        }
    }

    override fun showArrows(): Boolean {
        return false
    }
}