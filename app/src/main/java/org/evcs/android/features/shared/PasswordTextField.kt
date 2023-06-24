package org.evcs.android.features.shared

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import org.evcs.android.R

class PasswordTextField : StandardTextField {

    private val mMargin = resources.getDimension(R.dimen.spacing_large).toInt()
    private var mIsShowing = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs, defStyleAttr)

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        if (child is EditText) {
            child.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            addEye()
        }
    }

    private fun addEye() {
        val mEye = ImageView(context)
        mEye.setOnClickListener {
            mIsShowing = !mIsShowing
            if (mIsShowing) {
                editText?.inputType = InputType.TYPE_CLASS_TEXT
            } else {
                editText?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            editText?.setSelection(editText!!.length())
            mEye.setImageResource(if (mIsShowing) R.drawable.ic_show else R.drawable.ic_show)
        }
        val params =
            RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.setMargins(0, 0, mMargin, 0)
        mEye.setImageResource(R.drawable.ic_show)
        params.addRule(ALIGN_PARENT_END)
        params.addRule(CENTER_VERTICAL)
        mLayout.addView(mEye, -1, params)
    }
}