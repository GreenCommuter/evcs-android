package org.evcs.android.ui.view.shared

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import org.evcs.android.databinding.ViewCreditCardItemBinding
import org.evcs.android.model.PaymentMethod

class PaymentMethodView : LinearLayout {

    private lateinit var mBinding: ViewCreditCardItemBinding
    private lateinit var mPaymentMethod: PaymentMethod

    constructor(paymentMethod: PaymentMethod, context: Context) : super(context) {
        setPaymentMethod(paymentMethod)
        init(context)
    }


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun setPaymentMethod(paymentMethod: PaymentMethod) {
        mPaymentMethod = paymentMethod
//        mBinding.creditCardProvider.text = mPaymentMethod.card.brand.name
        mBinding.creditCardNumber.text = mPaymentMethod.card.last4
    }

    private fun init(context: Context) {
        mBinding = ViewCreditCardItemBinding.inflate(LayoutInflater.from(context), this, true)
        mBinding.creditCardDefault.visibility = View.GONE
        mBinding.viewListButtonChevron.visibility = View.GONE
        mBinding.creditCardChange.visibility = View.VISIBLE
    }

    fun setOnChangeClickListener(function: () -> Unit) {
        mBinding.creditCardChange.setOnClickListener { function.invoke() }
    }

}
