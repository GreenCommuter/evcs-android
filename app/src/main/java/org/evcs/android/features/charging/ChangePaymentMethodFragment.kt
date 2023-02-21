package org.evcs.android.features.charging

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.base.core.presenter.BasePresenter
import org.evcs.android.R
import org.evcs.android.databinding.FragmentChangePaymentMethodBinding
import org.evcs.android.model.PaymentMethod
import org.evcs.android.ui.fragment.ErrorFragment

class ChangePaymentMethodFragment : ErrorFragment<BasePresenter<*>>() {

    private lateinit var mList: LinearLayout
    private val mListener = ChargingNavigationController.getInstance()

    override fun layout(): Int {
        return R.layout.fragment_change_payment_method
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)//, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mList = FragmentChangePaymentMethodBinding.bind(v).changePaymentMethodLayout
    }

    override fun init() {
        val paymentMethods = requireArguments().getSerializable("payment_methods") as List<PaymentMethod>
        for (paymentMethod in paymentMethods) {
            val tv = TextView(requireContext())
            tv.setPadding(10, 10, 10, 10)
            tv.text = paymentMethod.card.provider.toPrintableString() + " ending in " + paymentMethod.card.last4
            tv.setOnClickListener { mListener.onPaymentMethodChanged(paymentMethod) }
            mList.addView(tv)
        }
    }

    interface PaymentMethodChangeListener {
        fun onPaymentMethodChanged(paymentMethod: PaymentMethod)
    }

}