package org.evcs.android.features.charging

import com.base.core.presenter.BasePresenter
import org.evcs.android.R

class OverLimitWarningFragment : WarningFragment<BasePresenter<*>>() {

    override fun getTitle(): CharSequence {
        return getString(R.string.over_limit_warning_title)
    }

    override fun getDescription(): CharSequence {
        return getText(R.string.over_limit_warning_text)
    }

}