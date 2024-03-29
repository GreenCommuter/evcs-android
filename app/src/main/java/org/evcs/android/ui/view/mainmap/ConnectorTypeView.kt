package org.evcs.android.ui.view.mainmap

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.RelativeLayout
import org.evcs.android.databinding.ViewConnectorTypeBinding
import org.evcs.android.model.ConnectorType

class ConnectorTypeView : FrameLayout {

    lateinit var connectorType: ConnectorType
    private lateinit var mLayout: FrameLayout

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
        binding.connectorTypeIcon.setImageDrawable(context.getDrawable(connectorType.mTextIcon!!))
        mLayout = binding.root
        isSelected = true
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        mLayout.isSelected = selected
    }

}