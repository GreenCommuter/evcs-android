package org.evcs.android.features.map

import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.base.core.fragment.BaseDialogFragment
import com.base.core.presenter.BasePresenter
import org.evcs.android.R
import org.evcs.android.databinding.DialogFilterBinding
import org.evcs.android.model.ConnectorType
import org.evcs.android.model.FilterState
import org.evcs.android.ui.view.mainmap.ConnectorTypeView

class FilterDialogFragment(private var mFilterState: FilterState = FilterState()) : BaseDialogFragment<BasePresenter<*>>() {

    private var mListener: FilterDialogListener? = null
    private lateinit var mBinding: DialogFilterBinding
    private var mMinKwValues : Array<Int> = arrayOf(0, 50, 70, 120)

    override fun layout(): Int {
        return R.layout.dialog_filter
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }

    override fun setUi(v: View) {
        mBinding = DialogFilterBinding.bind(v)
    }

    override fun init() {}

    override fun populate() {
        super.populate()
        for (i in 0..ConnectorType.values().size - 1) {
            val v = ConnectorTypeView(requireContext(), ConnectorType.values()[i])
            val param = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            mBinding.activityFilterConnectorTypes.addView(v, param)
            v.setOnClickListener {
                onConnectorClicked(v)
            }
            if (i < ConnectorType.values().size - 1) {
                val spacing = View(context)
                val params = LinearLayout.LayoutParams(0, -1, 0.2f)
                mBinding.activityFilterConnectorTypes.addView(spacing, params)
            }
        }

        mBinding.activityFilterMinPower.setLabels(
            mMinKwValues.map{ i -> if (i > 0) "$i" else "Any"}.toTypedArray())
        setFiltersFromState()
        keepStatusBar(mBinding.root)
        dimBackground()
    }

    private fun setFiltersFromState() {
        mBinding.activityFilterSwitch.isChecked = mFilterState.comingSoon ?: false
        mBinding.activityFilterMinPower.setProgress(mMinKwValues.indexOf(mFilterState.minKw))
        for (i in 0..mBinding.activityFilterConnectorTypes.childCount - 1) {
            val view = mBinding.activityFilterConnectorTypes.getChildAt(i)
            if (view is ConnectorTypeView)
                view.isSelected = mFilterState.connectorTypes.contains(view.connectorType)
        }
        onAnyFilterChanged()
    }

    private fun onConnectorClicked(v : ConnectorTypeView) {
        if (v.connectorType in mFilterState.connectorTypes) {
            v.isSelected = false
            mFilterState.connectorTypes.remove(v.connectorType)
        } else {
            v.isSelected = true
            mFilterState.connectorTypes.add(v.connectorType)
        }
        onAnyFilterChanged()
    }

    private fun onAnyFilterChanged() {
        mBinding.dialogFilterReset.isEnabled = !mFilterState.isEmpty()
    }

    override fun setListeners() {
        super.setListeners()

        mBinding.dialogFilterReset.setOnClickListener {
            mFilterState = FilterState()
            setFiltersFromState()
        }
        mBinding.activityFilterSwitch.setOnClickListener {
            mFilterState.comingSoon = mBinding.activityFilterSwitch.isChecked
            onAnyFilterChanged()
        }
        mBinding.activityFilterMinPower.setListener {
            mFilterState.minKw = mMinKwValues[mBinding.activityFilterMinPower.getProgress()]
            onAnyFilterChanged()
        }
        mBinding.dialogFilterClose.setOnClickListener { dismiss() }
        mBinding.activityFilterButton.setOnClickListener {
            mListener?.onFilterResult(mFilterState)
            dismiss()
        }
        mBinding.dialogFilterEmpty.setOnClickListener { dismiss() }
    }

    fun withListener(listener: FilterDialogListener): FilterDialogFragment {
        mListener = listener
        return this
    }

    interface FilterDialogListener {
        fun onFilterResult(mFilterState: FilterState)
    }

}

fun DialogFragment.keepStatusBar(rootView: View) {
    rootView.fitsSystemWindows = true
    val window = dialog!!.window!!
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
}

fun DialogFragment.dimBackground(amount: Float = 90/256.0f) {
    val window = dialog!!.window!!
    window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    window.setDimAmount(amount);
}