package org.evcs.android.features.shared

import org.evcs.android.R

class EVCSSliderDialogFragment : EVCSDialogFragment() {

    override fun layout(): Int {
        return R.layout.evcs_slider_dialog_fragment
    }

    class Builder : EVCSDialogFragment.Builder() {
        override fun addButton(text: String, listener: OnClickListener): EVCSDialogFragment.Builder {
            return addButton(text, listener)
        }

        override fun build(): EVCSSliderDialogFragment {
            val fragment = EVCSSliderDialogFragment()
            fragment.setParams(mTitleResource, mSubtitleResource, mButtons, mViews, mCancel,
                    mCancelOnClickListener, mCancelable)
            return fragment
        }
    }
}