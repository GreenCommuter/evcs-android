package org.evcs.android.ui.view.profile

import android.content.Context
import android.text.TextUtils
import android.widget.LinearLayout
import android.widget.TextView
import android.view.LayoutInflater
import android.util.AttributeSet
import org.evcs.android.R
import androidx.annotation.StringRes
import org.evcs.android.databinding.ViewListButtonBinding

class ListButtonView : LinearLayout {

    private lateinit var mText: TextView

    constructor(context: Context?) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val mBinding = ViewListButtonBinding.inflate(LayoutInflater.from(context), this, true)
        mText = mBinding.viewListButtonText
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ListButtonView)
        val text = typedArray.getString(R.styleable.ListButtonView_text)
        val label = typedArray.getString(R.styleable.ListButtonView_lbv_label)
        val bottomDiv = typedArray.getBoolean(R.styleable.ListButtonView_bottomDivider, true)
        val showButton = typedArray.getBoolean(R.styleable.ListButtonView_showButton, true)
        typedArray.recycle()
        mText.text = text
        mBinding.viewListButtonLabel.text = label
        mBinding.viewListButtonLabel.visibility = if (TextUtils.isEmpty(label)) GONE else VISIBLE
        mBinding.viewListButtonBottomLine.visibility = if (bottomDiv) VISIBLE else INVISIBLE
        mBinding.viewListButtonChevron.visibility = if (showButton) VISIBLE else GONE
        isClickable = showButton
    }

    fun setText(text: String?) {
        mText.text = text
    }

    fun setText(@StringRes resId: Int) {
        mText.setText(resId)
    }
}