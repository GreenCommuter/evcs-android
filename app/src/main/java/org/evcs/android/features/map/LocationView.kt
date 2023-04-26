package org.evcs.android.features.map

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import org.evcs.android.databinding.ViewListButtonBinding
import org.evcs.android.databinding.ViewLocationBinding
import org.evcs.android.model.Location
import org.evcs.android.util.LocationUtils

class LocationView : LinearLayout {
    private lateinit var mBinding: ViewLocationBinding

    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr) {
    }

    fun init(context: Context?) {
        mBinding = ViewLocationBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setLocation(location: Location) {
        mBinding.viewLocationTitle.text = location.name
        mBinding.viewLocationAddress.text = location.address.toString()
        mBinding.viewLocationPicture.setImageURI(location.imageUrls?.get(0))
        mBinding.viewLocationGo.setOnClickListener {
            LocationUtils.launchGoogleMapsWithPin(context, location.latLng)
        }
    }

    /*static*/ fun resize(view: View, height: Int) {
        val params = view.layoutParams
        params.height = height
        view.layoutParams = params
    }

    fun resizePicture(height: Int) {
        resize(mBinding.viewLocationPicture, height)
    }
}