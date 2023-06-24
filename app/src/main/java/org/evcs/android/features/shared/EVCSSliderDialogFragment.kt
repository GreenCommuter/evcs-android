package org.evcs.android.features.shared

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.view.isVisible
import org.evcs.android.R
import org.evcs.android.databinding.EvcsSliderDialogFragmentBinding
import org.evcs.android.features.map.keepStatusBar
import org.evcs.android.util.ViewUtils.setMargins

class EVCSSliderDialogFragment : EVCSDialogFragment() {

    private lateinit var mDivider: View
    private var mShowDivider: Boolean = false

    override fun layout(): Int {
        return R.layout.evcs_slider_dialog_fragment
    }

    override fun getCancelButton(cancel: String?): Button {
        val button = super.getCancelButton(cancel)
        button.setMargins(0, mButtonMargin, 0, mButtonMargin)
        button.background = resources.getDrawable(R.drawable.layout_corners_rounded_black_outline)
        return button
    }

    override fun getButton(label: String?): Button {
        val button = super.getButton(label)
        button.setMargins(0, mButtonMargin, 0, mButtonMargin)
        return button
    }

    private fun setShowDivider(show: Boolean) {
        mShowDivider = show
    }

    override fun setUi(v: View) {
        val binding: EvcsSliderDialogFragmentBinding = EvcsSliderDialogFragmentBinding.bind(v)
        mLayout = binding.evcsDialogFragmentLayout
        mTitle = binding.evcsDialogFragmentTitle
        mSubtitle = binding.evcsDialogFragmentSubtitle
        mDivider = binding.evcsDialogFragmentDivider.root
        keepStatusBar(binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDivider.isVisible = mShowDivider
    }

    class Builder : EVCSDialogFragment.Builder() {

        private var mShowDivider: Boolean = false

        fun showDivider(show: Boolean): Builder {
            mShowDivider = show
            return this
        }

        override fun setTitle(title: String?): EVCSDialogFragment.Builder {
            return super.setTitle(title, R.style.Title_Medium)
        }

        override fun build(): EVCSSliderDialogFragment {
            val fragment = EVCSSliderDialogFragment()
            fragment.setParams(
                mTitleResource, mTitleAppearance, mSubtitleResource, mButtons, mViews,
                mCancel, mCancelOnClickListener, mCancelable
            )
            fragment.setShowDivider(mShowDivider)
            return fragment
        }
    }

}
