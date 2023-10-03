package org.evcs.android.features.map.location_list

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.AdapterListLocationsBinding
import org.evcs.android.model.Location
import org.evcs.android.util.ViewUtils.setMargins

class LocationListViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

    private val mMargin: Int
    private lateinit var mLocation: Location
    private val mBinding: AdapterListLocationsBinding

    init {
        mBinding = AdapterListLocationsBinding.bind(itemView!!)
        mMargin = EVCSApplication.getInstance().applicationContext.resources
            .getDimension(R.dimen.spacing_medium_extra).toInt()
    }

    fun setLocation(location: Location) {
        mLocation = location
        mBinding.adapterSearchName.text = location.name
        mBinding.adapterSearchAddress.text = location.address.toString()
        val ac = location.stationCount!!.ac
        mBinding.adapterListAcAmount.visibility = if (ac > 0) View.VISIBLE else View.GONE
        mBinding.adapterListAcDesc.visibility = if (ac > 0) View.VISIBLE else View.GONE
        mBinding.adapterListAcAmount.text = ac.toString()
        val dc = location.stationCount!!.dc
        mBinding.adapterListDcAmount.visibility = if (dc > 0) View.VISIBLE else View.GONE
        mBinding.adapterListDcDesc.visibility = if (dc > 0) View.VISIBLE else View.GONE
        mBinding.adapterListDcAmount.text = dc.toString()

        val context = mBinding.adapterListConnectors.context
        mBinding.adapterListConnectors.removeAllViews()
        location.connectorTypes.forEach { connectorType ->
            val imageView = ImageView(context)
            imageView.setMargins(mMargin, 0, mMargin, 0)

            imageView.setImageDrawable(context.getDrawable(connectorType.mTextIcon))
            mBinding.adapterListConnectors.addView(imageView)
        }
    }

    fun setOnXClickListener(onXClickListener: ((Location) -> Unit)?) {
        mBinding.adapterSearchGo.setOnClickListener { onXClickListener?.invoke(mLocation) }
    }

}
