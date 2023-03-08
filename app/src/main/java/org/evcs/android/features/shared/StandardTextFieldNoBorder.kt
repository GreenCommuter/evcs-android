package org.evcs.android.features.shared

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.evcs.android.R

open class StandardTextFieldNoBorder : StandardTextField {

    private lateinit var mText: TextView

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs, defStyleAttr) {}

    override fun init(context: Context) {
        super.init(context)
        mLayout.background = null
        mLabel.background = null
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (child is TextView) {
            val childParams = LayoutParams(params)
            childParams.setMargins(0, resources.getDimension(R.dimen.spacing_small).toInt(), 0, 0)
            childParams.addRule(BELOW, R.id.standard_text_field_label)
            mLayout.addView(child, index, childParams)
            mText = child
        } else {
            super.addView(child, index, params)
        }
    }

    fun setText(text: String?) {
        mText.text = text
    }

}