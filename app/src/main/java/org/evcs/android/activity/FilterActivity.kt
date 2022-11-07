package org.evcs.android.activity

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import org.evcs.android.databinding.ActivityFilterBinding
import org.evcs.android.model.ConnectorType
import org.evcs.android.ui.view.shared.ConnectorTypeView


class FilterActivity : BaseActivity2() {
    private lateinit var mBinding: ActivityFilterBinding
    private var mSelectedConnectors : MutableSet<ConnectorType> = ConnectorType.values().toMutableSet()

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
            v.layoutParams = param

            mBinding.activityFilterConnectorTypes.addView(v)
            v.setOnClickListener {
                v.isSelected = !v.isSelected;
                toggle(v.connectorType)
            }
        }
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
            data.putExtra("Connector Types", mSelectedConnectors.toTypedArray())
            setResult(RESULT_OK, data)
            finish()
        }
    }

}