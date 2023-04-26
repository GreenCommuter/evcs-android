package org.evcs.android.features.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.MarkerLayoutBinding
import org.evcs.android.model.ClusterItemWithText
import org.evcs.android.model.ClusterItemWithValue


open class ClusterRenderer<T : ClusterItem>(private var mContext: Context, map: GoogleMap, clusterManager: ClusterManager<T>) :
    DefaultClusterRenderer<T>(mContext, map, clusterManager) {

    private var mSumItems: Boolean = false

    override fun onBeforeClusterItemRendered(item: T, markerOptions: MarkerOptions) {
        markerOptions.icon(getBitmap(item, R.drawable.ic_map_pin))
        //If items have the same z index the one at the back is selected, for some reason
//        markerOptions.zIndex((90 - item.position.latitude).toFloat())
    }

    //Show exact number
    override fun getClusterText(bucket: Int): String {
        return bucket.toString()
    }

    override fun getBucket(cluster: Cluster<T>): Int {
        return cluster.size
    }

    fun onClusterItemChange(item: T, marker: Marker, isSelected : Boolean) {
        val icon = if (isSelected) R.drawable.ic_map_pin_selected else R.drawable.ic_map_pin
        marker.setIcon(getBitmap(item, icon))
    }

    override fun onBeforeClusterRendered(cluster: Cluster<T>, markerOptions: MarkerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions)
        var text = cluster.size.toString()
        if (mSumItems && cluster.items.first() is ClusterItemWithValue) {
            text = (cluster.items as Collection<ClusterItemWithValue>)
                .sumOf { item -> item.markerValue }.toString()
        }
        val bitmap = createMarker(text, R.drawable.layout_oval_orange,
                Gravity.CENTER, Color.WHITE)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap))
    }

    private fun getBitmap(item: T, @DrawableRes icon: Int): BitmapDescriptor? {
        if (item is ClusterItemWithText) {
            return BitmapDescriptorFactory.fromBitmap(createMarker(item.markerText, icon))
        } else {
            return BitmapDescriptorFactory.fromResource(icon)
        }
    }

    private fun createMarker(text: String, @DrawableRes icon: Int,
                             textAlign: Int = Gravity.CENTER_HORIZONTAL, textColor: Int = Color.WHITE): Bitmap {
        val markerLayout = buildLayout(text, icon, textAlign, textColor)
        markerLayout.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        markerLayout.layout(0, 0, markerLayout.measuredWidth, markerLayout.measuredHeight)
        val bitmap = Bitmap.createBitmap(markerLayout.measuredWidth, markerLayout.measuredHeight,
            Bitmap.Config.ARGB_8888)
        markerLayout.draw(Canvas(bitmap))
        return bitmap
    }

    private fun buildLayout(text: String, @DrawableRes icon: Int, textAlign: Int, textColor: Int): ViewGroup {
        val binding = MarkerLayoutBinding.inflate(LayoutInflater.from(mContext))
        val markerLayout = binding.root
        binding.markerImage.setImageResource(icon)
        binding.markerText.text = text
        binding.markerText.setTextColor(textColor)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        params.gravity = textAlign
        binding.markerText.typeface = Typeface.DEFAULT
        binding.markerText.layoutParams = params
        if (textAlign == Gravity.CENTER_HORIZONTAL) {
            val paddingTop = EVCSApplication.getInstance().applicationContext
                    .resources.getDimension(R.dimen.spacing_medium).toInt()
            binding.markerText.setPadding(0, paddingTop, 0 ,0)
        }
        return markerLayout
    }

    fun setSumItems(sumItems: Boolean) {
        mSumItems = sumItems
    }

}
