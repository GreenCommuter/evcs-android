package org.evcs.android.features.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
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
import org.evcs.android.R
import org.evcs.android.databinding.MarkerLayoutBinding
import org.evcs.android.model.ClusterItemWithText
import java.util.*


class ClusterRenderer<T : ClusterItem>(private var mContext: Context, map: GoogleMap, clusterManager: ClusterManager<T>) :
    DefaultClusterRenderer<T>(mContext, map, clusterManager) {

    override fun onBeforeClusterItemRendered(item: T, markerOptions: MarkerOptions) {
        markerOptions.icon(getBitmap(item, R.drawable.ic_map_pin_unselected))
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
        val icon = if (isSelected) R.drawable.ic_map_orange else R.drawable.ic_map_pin_unselected
        marker.setIcon(getBitmap(item, icon))
    }

    private fun getBitmap(item: T, @DrawableRes icon: Int): BitmapDescriptor? {
        if (item is ClusterItemWithText) {
            return BitmapDescriptorFactory.fromBitmap(createMarker(item.markerText, icon))
        } else {
            return BitmapDescriptorFactory.fromResource(icon)
        }
    }

    private fun createMarker(text: String, @DrawableRes icon: Int): Bitmap? {
        val binding = MarkerLayoutBinding.inflate(LayoutInflater.from(mContext))
        val markerLayout = binding.root
        binding.markerImage.setImageResource(icon)
        binding.markerText.text = text
        markerLayout.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        markerLayout.layout(0, 0, markerLayout.measuredWidth, markerLayout.measuredHeight)
        val bitmap = Bitmap.createBitmap(markerLayout.measuredWidth, markerLayout.measuredHeight,
            Bitmap.Config.ARGB_8888)
        markerLayout.draw(Canvas(bitmap))
        return bitmap
    }
}
