package org.evcs.android.activity

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import org.evcs.android.R
import org.evcs.android.databinding.ActivityFilterBinding
import org.evcs.android.model.ConnectorType
import org.evcs.android.ui.view.shared.ConnectorTypeView
import org.evcs.android.util.Extras


class FilterActivity : BaseActivity2() {
    private lateinit var mBinding: ActivityFilterBinding
    private var mSelectedConnectors : MutableSet<ConnectorType> = ConnectorType.values().toMutableSet()
    private var mMinKwValues : Array<Int> = arrayOf(0, 50, 70, 120)

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivityFilterBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun init() {
    }

    override fun populate() {
        super.populate()
        for (connectorType in ConnectorType.values()) {
            var v = ConnectorTypeView(this, connectorType)
            val param = GridLayout.LayoutParams(
                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
            )
            param.height = GridLayout.LayoutParams.WRAP_CONTENT
            param.width = 0

            mBinding.activityFilterConnectorTypes.addView(v, param)
            v.setOnClickListener {
                v.isSelected = !v.isSelected
                toggle(v.connectorType)
            }
        }
        mBinding.activityFilterToolbar.title = getString(R.string.filter_activity_title)
        mBinding.activityFilterToolbar.navigationIcon = resources.getDrawable(R.drawable.new_close)
        mBinding.activityFilterToolbar.setNavigationOnClickListener { finish() }

        mBinding.activityFilterMinPower.setLabels(
            mMinKwValues.map{ i -> if (i > 0) "$i"+"kW" else "Any"}.toTypedArray());
    }

    private fun toggle(connectorType: ConnectorType) {
        if (connectorType in mSelectedConnectors) {
            mSelectedConnectors.remove(connectorType)
        } else {
            mSelectedConnectors.add(connectorType)
        }
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.activityFilterButton.setOnClickListener {
            var data = Intent()
            data.putExtra(Extras.FilterActivity.CONNECTOR_TYPES, mSelectedConnectors.toTypedArray())
            data.putExtra(Extras.FilterActivity.MIN_KW, mMinKwValues[mBinding.activityFilterMinPower.seekbar.progress])
            setResult(RESULT_OK, data)
            finish()
        }
        for (i in 0 until mBinding.activityFilterAuthorities.childCount) {
            val child = mBinding.activityFilterAuthorities.getChildAt(i)
            child.setOnClickListener{ child.isSelected = !child.isSelected }
        }
    }

}