package org.evcs.android.features.shared

import android.widget.Button
import org.evcs.android.R
import org.evcs.android.util.ViewUtils.setMargins

class EVCSSliderDialogFragment : EVCSDialogFragment() {

    override fun layout(): Int {
        return R.layout.evcs_slider_dialog_fragment
    }

    override fun getCancelButton(cancel: String?): Button {
        val button = super.getCancelButton(cancel)
        button.setMargins(mButtonMargin, mButtonMargin, mButtonMargin, mButtonMargin)
        button.background = resources.getDrawable(R.drawable.layout_corners_rounded_black_outline)
        return button
    }

    class Builder : EVCSDialogFragment.Builder() {
        override fun build(): EVCSSliderDialogFragment {
            val fragment = EVCSSliderDialogFragment()
            fragment.setParams(mTitleResource, mSubtitleResource, mButtons, mViews, mCancel,
                    mCancelOnClickListener, mCancelable)
            return fragment
        }
    }
}