package org.evcs.android.ui.view.shared

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import org.evcs.android.R
import org.evcs.android.databinding.ViewCouponCodeBinding

class CouponCodeView : LinearLayout {

    private var mListener: ((String) -> Unit)? = null
    private lateinit var mBinding: ViewCouponCodeBinding
    private var isEditable = true

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
    }

    fun init(context: Context) {
        val apply = context.getString(R.string.coupon_code_apply)
        val remove = context.getString(R.string.coupon_code_remove)
        mBinding = ViewCouponCodeBinding.inflate(LayoutInflater.from(context), this, true)
        mBinding.couponCodeText.editText?.addTextChangedListener { text ->
            mBinding.couponCodeApplyRemove.visibility = if (text!!.isEmpty()) View.GONE else View.VISIBLE
        }
        mBinding.couponCodeApplyRemove.setOnClickListener {
            if (!isEditable) { mBinding.couponCodeText.editText!!.setText("") }
            isEditable = !isEditable
            mBinding.couponCodeApplyRemove.text = if (isEditable) apply else remove
            mBinding.couponCodeText.editText!!.isEnabled = isEditable
            mListener?.invoke(mBinding.couponCodeText.text.toString())
        }
    }

    fun setListener(listener: (String) -> Unit) {
        mListener = listener
    }
}