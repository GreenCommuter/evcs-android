package org.evcs.android.features.charging

import android.graphics.Color
import android.view.View
import android.widget.TextView
import org.evcs.android.ui.fragment.ErrorFragment
import com.base.core.presenter.BasePresenter
import org.evcs.android.R
import org.evcs.android.databinding.FragmentAccountSuspendedBinding
import org.evcs.android.util.FontUtils
import org.evcs.android.util.PaymentUtils

class AccountSuspendedFragment : ErrorFragment<BasePresenter<*>?>() {

    private lateinit var mSubtitle: TextView
    private lateinit var mButton: TextView

    override fun layout(): Int {
        return R.layout.fragment_account_suspended
    }

    override fun setUi(v: View) {
        val binding = FragmentAccountSuspendedBinding.bind(v)
        mButton = binding.accountSuspendedButton
        mSubtitle = binding.accountSuspendedSubtitle
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter<Any?>(this)
    }

    override fun init() {}

    override fun populate() {
        mSubtitle.text = FontUtils.getSpannable(
            resources.getStringArray(R.array.account_suspended_subtitle), Color.BLACK)
    }

    override fun setListeners() {
        mButton.setOnClickListener { PaymentUtils.goToPendingPayment(requireContext()) }
    }

    override fun onBackPressed(): Boolean {
        return true
    }
}