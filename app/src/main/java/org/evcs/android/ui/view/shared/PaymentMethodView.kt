package org.evcs.android.ui.view.shared

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import org.evcs.android.databinding.ViewPaymentMethodBinding
import org.evcs.android.model.PaymentMethod

class PaymentMethodView : LinearLayout {

    private lateinit var mPaymentMethod: PaymentMethod

    constructor(paymentMethod: PaymentMethod, context: Context) : super(context) {
        mPaymentMethod = paymentMethod
        init(context)
    }


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        val binding = ViewPaymentMethodBinding.inflate(LayoutInflater.from(context), this, true)
        binding.paymentMethodBrand.text = mPaymentMethod.card.brand
        binding.paymentMethodNumber.text = mPaymentMethod.card.last4
    }

}
