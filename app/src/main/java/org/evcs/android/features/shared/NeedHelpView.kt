package org.evcs.android.features.shared

import android.content.Context
import android.util.AttributeSet

class NeedHelpView : androidx.appcompat.widget.AppCompatTextView {
    constructor(context: Context) : super(context) {
        init(context)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    private fun init(context: Context) {

    }
}