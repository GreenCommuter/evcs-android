package org.evcs.android.features.shared

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import org.evcs.android.R
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

open class StandardTextFieldNoBorder : StandardTextField {

    private lateinit var mText: TextView

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs, defStyleAttr) {}

    constructor(context: Context, label: String, text: String, @StyleRes textStyle: Int = R.style.Body_Medium) :
        super(context) {
        setLabel(label)
        val tv = TextView(context)
        tv.text = text
        tv.setTextAppearance(context, textStyle)
        addView(tv)
    }

    override fun init(context: Context) {
        super.init(context)
        mLayout.background = null
        mLayout.setPadding(0, 0, 0, 0)
        mLabel.background = null
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (child is TextView) {
            val childParams = LayoutParams(params)
            childParams.setMargins(0, 0, 0, 0)
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

    fun setText(text: DateTime, @StringRes format: Int = R.string.app_date_format) {
        mText.setText(text, format)
    }

}

fun TextView.setText(dateTime: DateTime, @StringRes format: Int = R.string.app_date_format) {
    val dateTimeFormatter = DateTimeFormat.forPattern(context.getString(format))
    text = dateTimeFormatter.print(dateTime)
}
