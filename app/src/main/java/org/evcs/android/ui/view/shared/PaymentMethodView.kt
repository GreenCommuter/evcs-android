package org.evcs.android.ui.view.shared

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import org.evcs.android.R
import org.evcs.android.databinding.ViewCreditCardItemBinding
import org.evcs.android.model.PaymentMethod

class PaymentMethodView : LinearLayout {

    private var mListener: OnClickListener? = null
    private lateinit var mBinding: ViewCreditCardItemBinding

    constructor(paymentMethod: PaymentMethod, context: Context) : super(context) {
        init(context)
        setPaymentMethod(paymentMethod)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
        setPaymentMethod(null)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun setPaymentMethod(paymentMethod: PaymentMethod?) {
        if (paymentMethod == null) {
            setAddPaymentMethod()
        } else if (paymentMethod.isGpay) {
            setGooglePay()
        } else {
            mBinding.viewListButtonChevron.visibility = View.GONE
            mBinding.creditCardChange.visibility = View.VISIBLE
            mBinding.creditCardProvider.setImageDrawable(resources.getDrawable(paymentMethod.card.brand?.drawable))
            mBinding.creditCardNumber.text = String.format("•••• %s", paymentMethod.card.last4)
            mBinding.creditCardChange.setOnClickListener{ mListener?.onClick(null) }
        }
    }

    private fun setAddPaymentMethod() {
        mBinding.creditCardProvider.setImageDrawable(resources.getDrawable(R.drawable.ic_add))
        val params = mBinding.creditCardProvider.layoutParams
        params.width = LayoutParams.WRAP_CONTENT
        mBinding.creditCardProvider.layoutParams = params
        mBinding.creditCardNumber.text = resources.getString(R.string.payment_method_toolbar_add)
        mBinding.root.setOnClickListener { mListener?.onClick(null) }
    }

    private fun init(context: Context) {
        mBinding = ViewCreditCardItemBinding.inflate(LayoutInflater.from(context), this, true)
        mBinding.creditCardDefault.visibility = View.GONE
    }

    fun setOnChangeClickListener(function: OnClickListener) {
        mListener = function
    }

    fun setGooglePay() {
        mBinding.viewListButtonChevron.isVisible = false
        mBinding.creditCardNumber.text = "Google Pay"
        mBinding.creditCardProvider.setImageDrawable(resources.getDrawable(R.drawable.cc_gpay))
        mBinding.root.setOnClickListener { mListener?.onClick(null) }
    }

}
