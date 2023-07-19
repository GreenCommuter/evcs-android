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
                val params = LinearLayout.LayoutParams(resources.getDimension(R.dimen.spacing_medium_extra).toInt(), -1)
                mBinding.activityFilterConnectorTypes.addView(spacing, params)
            }
        }

        mBinding.activityFilterMinPower.setLabels(
            mMinKwValues.map{ i -> if (i > 0) "$i" else "Any"}.toTypedArray())
        mBinding.activityFilterMinPower.seekbar.progressDrawable =
            context?.getDrawable(R.drawable.progress_bar_background)
        setFiltersFromState()
        keepStatusBar(mBinding.root)
    }

    private fun setFiltersFromState() {
        mBinding.activityFilterSwitch.isChecked = mFilterState.comingSoon ?: false
        mBinding.activityFilterMinPower.seekbar.progress = mMinKwValues.indexOf(mFilterState.minKw)
        for (i in 0..mBinding.activityFilterConnectorTypes.childCount - 1) {
            val view = mBinding.activityFilterConnectorTypes.getChildAt(i)
            if (view is ConnectorTypeView)
                view.isSelected = view.connectorType == mFilterState.connectorType
                        || mFilterState.connectorType == null
        }
        onAnyFilterChanged()
    }

    private fun onConnectorClicked(v : ConnectorTypeView) {
        for (i in 0 .. mBinding.activityFilterConnectorTypes.childCount - 1)
            mBinding.activityFilterConnectorTypes.getChildAt(i).isSelected = false
        v.isSelected = true
        mFilterState.connectorType = v.connectorType
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
        mBinding.activityFilterMinPower.setOnSeekBarChangeListener {
            mFilterState.minKw = mMinKwValues[mBinding.activityFilterMinPower.seekbar.progress]
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
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
}