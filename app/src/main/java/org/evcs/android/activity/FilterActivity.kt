package org.evcs.android.activity

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import org.evcs.android.R
import org.evcs.android.databinding.ActivityFilterBinding
import org.evcs.android.model.ConnectorType
import org.evcs.android.model.FilterState
import org.evcs.android.ui.view.shared.ConnectorTypeView
import org.evcs.android.util.Extras


class FilterActivity : BaseActivity2() {
    private lateinit var mBinding: ActivityFilterBinding
    private var mMinKwValues : Array<Int> = arrayOf(0, 50, 70, 120)
    private var mFilterState : FilterState = FilterState()

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivityFilterBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun init() {
        mFilterState = intent.extras?.getSerializable(Extras.FilterActivity.FILTER_STATE) as FilterState
    }

    override fun populate() {
        super.populate()
        for (connectorType in ConnectorType.values()) {
            val v = ConnectorTypeView(this, connectorType)
            val param = GridLayout.LayoutParams(
                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
            )
            param.height = GridLayout.LayoutParams.WRAP_CONTENT
            param.width = 0

            mBinding.activityFilterConnectorTypes.addView(v, param)
            v.setOnClickListener {
                onConnectorClicked(v)
            }
        }
        mBinding.activityFilterToolbar.title = getString(R.string.filter_activity_title)
        mBinding.activityFilterToolbar.navigationIcon = resources.getDrawable(R.drawable.ic_xmark_solid)
        mBinding.activityFilterToolbar.inflateMenu(R.menu.filter_activity_toolbar)

        mBinding.activityFilterMinPower.setLabels(
            mMinKwValues.map{ i -> if (i > 0) "$i"+"kW" else "Any"}.toTypedArray())
        mBinding.activityFilterMinPower.seekbar.progressDrawable =
            resources.getDrawable(R.drawable.progress_bar_background)
        setFiltersFromState()
    }

    private fun setFiltersFromState() {
        mBinding.activityFilterSwitch.isChecked = mFilterState.onlyAvailable
        mBinding.activityFilterMinPower.seekbar.progress = mMinKwValues.indexOf(mFilterState.minKw)
        for (i in 0..mBinding.activityFilterConnectorTypes.childCount - 1) {
            val view = mBinding.activityFilterConnectorTypes.getChildAt(i) as ConnectorTypeView
            view.isSelected = view.connectorType == mFilterState.connectorType || mFilterState.connectorType == null
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
        mBinding.activityFilterToolbar.setOnMenuItemClickListener {
            mFilterState = FilterState()
            setFiltersFromState()
            true
        }
        mBinding.activityFilterToolbar.setNavigationOnClickListener { finish() }
        mBinding.activityFilterButton.setOnClickListener {
            val data = Intent()
            mFilterState.minKw = mMinKwValues[mBinding.activityFilterMinPower.seekbar.progress]
            mFilterState.onlyAvailable = mBinding.activityFilterSwitch.isChecked
            data.putExtra(Extras.FilterActivity.FILTER_STATE, mFilterState)
            setResult(RESULT_OK, data)
            finish()
        }
        for (i in 0 until mBinding.activityFilterAuthorities.childCount) {
            val child = mBinding.activityFilterAuthorities.getChildAt(i)
            child.setOnClickListener{ child.isSelected = !child.isSelected }
        }
    }

}