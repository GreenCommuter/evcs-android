package org.evcs.android.activity.location_list

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.AdapterListLocationsBinding
import org.evcs.android.model.Location

class LocationListViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

    private val mMargin: Int
    private lateinit var mLocation: Location
    private val mBinding: AdapterListLocationsBinding

    init {
        mBinding = AdapterListLocationsBinding.bind(itemView!!)
        mMargin = EVCSApplication.getInstance().applicationContext.resources
            .getDimension(R.dimen.spacing_large).toInt()
    }

    fun setLocation(location: Location) {
        mLocation = location
        mBinding.adapterSearchName.text = location.name
        mBinding.adapterSearchAddress.text = location.address.toString()
        val ac = location.stationCount!!.ac
        mBinding.adapterListAcAmount.visibility = if (ac > 0) View.VISIBLE else View.GONE
        mBinding.adapterListAcDesc.visibility = if (ac > 0) View.VISIBLE else View.GONE
        mBinding.adapterListAcAmount.text = ac.toString()
        val dc = location.stationCount!!.totalDc()
        mBinding.adapterListDcAmount.visibility = if (dc > 0) View.VISIBLE else View.GONE
        mBinding.adapterListDcDesc.visibility = if (dc > 0) View.VISIBLE else View.GONE
        mBinding.adapterListDcAmount.text = dc.toString()

        val context = mBinding.adapterListConnectors.context
        mBinding.adapterListConnectors.removeAllViews()
        location.connectorTypes.forEach { connectorType ->
            val imageView = ImageView(context)
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(mMargin, 0, mMargin, 0)
            imageView.layoutParams = layoutParams

            imageView.setImageDrawable(context.getDrawable(connectorType.mTextIcon))
            mBinding.adapterListConnectors.addView(imageView)
        }
    }

    fun setOnXClickListener(onXClickListener: ((Location) -> Unit)?) {
        mBinding.adapterSearchGo.setOnClickListener { onXClickListener?.invoke(mLocation) }
    }

}
