package org.evcs.android.ui.view.shared

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import org.evcs.android.BaseConfiguration
import org.evcs.android.R
import org.evcs.android.activity.WebViewActivity
import org.evcs.android.databinding.ViewLyftBinding
import org.evcs.android.util.UserUtils

class LyftView : LinearLayout {

    private lateinit var mBinding: ViewLyftBinding

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    private fun init() {
        //make sure set is called at least once, so that we hide the view if needed
        setVisibility(visibility)
        mBinding = ViewLyftBinding.inflate(LayoutInflater.from(context), this, true)
        val stringArray = resources.getStringArray(R.array.lyft_array)
        mBinding.viewLyft1.text = getSpannable(stringArray, resources.getColor(R.color.lyft_pink))

        mBinding.viewLyft2.setOnClickListener {
            val intent = WebViewActivity.buildIntent(context, "", BaseConfiguration.WebViews.LYFT_URL, "")
            context.startActivity(intent)
        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(if (UserUtils.getLoggedUser().isLyftUser()) visibility else View.GONE)
    }

    fun getSpannable(stringArray: Array<String>, color: Int): SpannableString {
        val text1 = stringArray[0]
        val text2 = stringArray[1]
        val text3 = stringArray[2]
        val spannable = SpannableString("$text1 $text2$text3")
        val start = text1.length
        val end = start + text2.length + 1
        spannable.setSpan(ForegroundColorSpan(color), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        return spannable
    }
}