package org.evcs.android.features.profile.wallet

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.base.core.fragment.BaseFragment
import com.base.core.presenter.BasePresenter
import com.base.core.util.ToastUtils
import com.stripe.android.PaymentConfiguration
import com.stripe.android.googlepaylauncher.GooglePayPaymentMethodLauncher
import org.evcs.android.Configuration
import org.evcs.android.R
import org.evcs.android.databinding.FragmentAddPaymentMethodBinding
import org.evcs.android.navigation.controller.AbstractNavigationController
import org.evcs.android.ui.view.shared.EVCSToolbar2
import org.evcs.android.ui.view.shared.PaymentMethodView

class AddPaymentMethodFragment : BaseFragment<BasePresenter<*>>() {

    private lateinit var mGooglePayLauncher: GooglePayPaymentMethodLauncher
    private lateinit var mCreditCard: PaymentMethodView
    private lateinit var mGooglePay: PaymentMethodView
    private lateinit var mToolbar: EVCSToolbar2

    companion object {
        fun newInstance(): AddPaymentMethodFragment {
            val args = Bundle()
            val fragment = AddPaymentMethodFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun layout(): Int {
        return R.layout.fragment_add_payment_method
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }

    override fun init() {
        setUpGpay()
    }

    override fun setUi(v: View) {
        super.setUi(v)
        val binding = FragmentAddPaymentMethodBinding.bind(v)
        mToolbar = binding.addPaymentMethodToolbar
        mGooglePay = binding.addPaymentMethodGpay
        mCreditCard = binding.addPaymentMethodCard
    }

    override fun populate() {
        mGooglePay.setGooglePay()
        if (arguments?.getBoolean("has_google_pay") ?: false) mGooglePay.isVisible = false
        mCreditCard.setGeneric()
    }

    override fun setListeners() {
        mToolbar.setNavigationOnClickListener { activity?.finish() }
        mCreditCard.setOnChangeClickListener { onAddPaymentMethodSelected() }
        mGooglePay.setOnChangeClickListener {
            try {
                mGooglePayLauncher.present(currencyCode = "USD")
            } catch (e : java.lang.IllegalStateException) {
                ToastUtils.show("Google Pay not installed")
            }
        }
    }

    private fun onAddPaymentMethodSelected() {
        val navOptions = AbstractNavigationController.replaceLastNavOptions(findNavController())
        findNavController().
            navigate(AddPaymentMethodFragmentDirections.actionAddPaymentMethodFragmentToAddCreditCardFragment(), navOptions)
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
        Log.d("Google play ready", isReady.toString())
//        mBinding.planInfoGpay.isEnabled = isReady
    }

    private fun onGooglePayResult(result: GooglePayPaymentMethodLauncher.Result) {
        when (result) {
            is GooglePayPaymentMethodLauncher.Result.Completed -> {
                // TODO
            }
            GooglePayPaymentMethodLauncher.Result.Canceled -> {
                // User canceled the operation
            }
            is GooglePayPaymentMethodLauncher.Result.Failed -> {
                // Operation failed; inspect `result.error` for the exception
            }
        }
    }

}