package org.evcs.android.ui.view.shared

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import org.evcs.android.databinding.ViewReceiptItemBinding

class ReceiptItemView : LinearLayout {

    private lateinit var mBinding: ViewReceiptItemBinding

    constructor(context: Context, label: String?, description: String?, amount: String?) : super(context) {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        init(context)
        set(label, description, amount)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun set(label: String?, description: String?, amount: String?) {
        mBinding.viewReceiptItemLabel.text = label
        mBinding.viewReceiptItemDescription.text = description
        mBinding.viewReceiptItemAmount.text = amount
    }

    private fun init(context: Context) {
        mBinding = ViewReceiptItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

}
