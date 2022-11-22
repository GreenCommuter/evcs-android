package org.evcs.android.ui.view.shared

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import org.evcs.android.databinding.ViewConnectorTypeBinding
import org.evcs.android.model.ConnectorType

class ConnectorTypeView : RelativeLayout {

    lateinit var connectorType: ConnectorType
    private lateinit var mLayout: RelativeLayout

    constructor(context: Context, connectorType : ConnectorType) : super(context) {
        this.connectorType = connectorType;
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs, defStyleAttr) {
        init(context)
    }

    protected fun init(context: Context) {
        val binding = ViewConnectorTypeBinding.inflate(LayoutInflater.from(context), this, true)
        binding.connectorTypeText.text = connectorType.printableName
        binding.connectorTypeIcon.setImageDrawable(context.resources.getDrawable(connectorType.mIcon!!))
        binding.connectorTypeAc.text = if (connectorType.mIsAc) "Ac" else "Dc"
        mLayout = binding.connectorTypeAc.parent as RelativeLayout
        isSelected = true
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        mLayout.isSelected = selected
    }

}