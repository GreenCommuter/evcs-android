package org.evcs.android.features.map

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import org.evcs.android.databinding.AdapterMapCarouselItemBinding
import org.evcs.android.databinding.ViewConnectorTypeBinding
import org.evcs.android.model.ConnectorType
import org.evcs.android.model.Location

class LocationItemView : RelativeLayout {

    private var location: Location? = null
    private lateinit var mBinding: AdapterMapCarouselItemBinding

    constructor(context: Context, location: Location) : super(context) {
        this.location = location;
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
        mBinding = AdapterMapCarouselItemBinding.inflate(LayoutInflater.from(context), this, true)
        if (location != null) setLocation(location!!)
    }

    fun setLocation(location: Location) {
        mBinding.adapterMapName.text = location.name
        mBinding.adapterMapDistance.text = location.printableDistance
        mBinding.adapterMapAddress.text = location.address.toString()
    }
}