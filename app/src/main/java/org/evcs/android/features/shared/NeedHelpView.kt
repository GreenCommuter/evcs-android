package org.evcs.android.features.shared

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import com.base.core.util.NavigationUtils
import org.evcs.android.R
import org.evcs.android.activity.ContactSupportActivity

class NeedHelpView : androidx.appcompat.widget.AppCompatTextView {

    constructor(context: Context) : super(context) {
        init(context)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    private fun init(context: Context) {
        setTextAppearance(context, R.style.Label_Small)
        val medium = context.resources.getDimension(R.dimen.spacing_medium).toInt()
        setPadding(medium, medium, medium, medium)
        gravity = Gravity.CENTER
        text = context.getText(R.string.session_information_help)
        setOnClickListener { NavigationUtils.jumpTo(context, ContactSupportActivity::class.java) }
    }
}