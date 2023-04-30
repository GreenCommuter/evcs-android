package org.evcs.android.features.map

import android.view.View
import android.widget.LinearLayout
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

    override fun setUi(v: View?) {
        mBinding = DialogFilterBinding.bind(v!!)
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
                val params = LinearLayout.LayoutParams(resources.getDimension(R.dimen.spacing_large).toInt(), -1)
                mBinding.activityFilterConnectorTypes.addView(spacing, params)
            }
        }

        mBinding.activityFilterMinPower.setLabels(
            mMinKwValues.map{ i -> if (i > 0) "$i"+"kW" else "Any"}.toTypedArray())
        mBinding.activityFilterMinPower.seekbar.progressDrawable =
            resources.getDrawable(R.drawable.progress_bar_background)
        setFiltersFromState()
    }

    private fun setFiltersFromState() {
        mBinding.activityFilterSwitch.isChecked = mFilterState.comingSoon
        mBinding.activityFilterMinPower.seekbar.progress = mMinKwValues.indexOf(mFilterState.minKw)
        for (i in 0..mBinding.activityFilterConnectorTypes.childCount - 1) {
            val view = mBinding.activityFilterConnectorTypes.getChildAt(i)
            if (view is ConnectorTypeView)
                view.isSelected = view.connectorType == mFilterState.connectorType
                        || mFilterState.connectorType == null
        }
    }

    private fun onConnectorClicked(v : ConnectorTypeView) {
        for (i in 0 .. mBinding.activityFilterConnectorTypes.childCount - 1)
            mBinding.activityFilterConnectorTypes.getChildAt(i).isSelected = false
        v.isSelected = true
        mFilterState.connectorType = v.connectorType
    }

    override fun setListeners() {
        super.setListeners()

        mBinding.dialogFilterReset.setOnClickListener {
            mFilterState = FilterState()
            setFiltersFromState()
        }
        mBinding.dialogFilterClose.setOnClickListener { dismiss() }
        mBinding.activityFilterButton.setOnClickListener {
            mFilterState.minKw = mMinKwValues[mBinding.activityFilterMinPower.seekbar.progress]
            mFilterState.comingSoon = mBinding.activityFilterSwitch.isChecked
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