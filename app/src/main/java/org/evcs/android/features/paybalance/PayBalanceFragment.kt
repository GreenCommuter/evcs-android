package org.evcs.android.features.paybalance

import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import com.base.core.util.NavigationUtils
import com.base.core.util.ToastUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentPayBalanceBinding
import org.evcs.android.features.main.MainActivity
import org.evcs.android.features.profile.wallet.WalletActivity
import org.evcs.android.model.Payment
import org.evcs.android.model.PaymentMethod
import org.evcs.android.ui.fragment.ErrorFragment

class PayBalanceFragment : ErrorFragment<PayBalancePresenter>(), PayBalanceView {

    private var mSelectedPM: PaymentMethod? = null
    private lateinit var mBinding: FragmentPayBalanceBinding
    private lateinit var mLauncher: ActivityResultLauncher<Intent>

    override fun layout(): Int {
        return R.layout.fragment_pay_balance
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = FragmentPayBalanceBinding.bind(v)
    }

    override fun init() {
        mLauncher = WalletActivity.getDefaultLauncher(this) { pm ->
            mSelectedPM = pm
            mBinding.payBalancePaymentMethod.setPaymentMethod(pm)
            mBinding.payBalanceProcessPayment.isEnabled = true
        }
    }

    override fun populate() {
        showProgressDialog()
        mSelectedPM = PaymentMethod.getDefaultFromSharedPrefs()
        mBinding.payBalancePaymentMethod.setPaymentMethod(mSelectedPM)
        presenter?.getPendingPayment()
    }

    override fun setListeners() {
        mBinding.payBalancePaymentMethod.setOnChangeClickListener {
            val intent = WalletActivity.buildIntent(context, true)
            mLauncher.launch(intent)
        }
        mBinding.payBalanceProcessPayment.setOnClickListener { presenter?.processPayment() }
    }

    override fun createPresenter(): PayBalancePresenter {
        return PayBalancePresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun onPaymentSuccess() {
        ToastUtils.show(getString(R.string.pay_balance_success))
        NavigationUtils.jumpToClearingTask(requireContext(), MainActivity::class.java)
    }

    override fun showPendingPayment(payment: Payment) {
        hideProgressDialog()
        mBinding.payBalanceDescription.text = payment.description
        mBinding.payBalanceAmount.text = payment.amount.toString()
    }

}
