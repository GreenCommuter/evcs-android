package org.evcs.android.features.shared

import android.content.Context
import android.graphics.Typeface
import android.os.Handler
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class ThinButton : AppCompatButton {
    constructor(context: Context) : super(context) { init() }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr ) {
        init()
    }
    private fun init() {
        Handler().post { setTypeface(null, Typeface.NORMAL) }
    }

}