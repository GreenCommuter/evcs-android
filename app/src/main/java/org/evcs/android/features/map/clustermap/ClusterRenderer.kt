package org.evcs.android.features.map.clustermap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
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
import org.evcs.android.R
import org.evcs.android.databinding.MarkerLayoutBinding
import org.evcs.android.util.BitmapUtils


open class ClusterRenderer<T : ClusterItem>(private var mContext: Context, map: GoogleMap, clusterManager: ClusterManager<T>) :
    DefaultClusterRenderer<T>(mContext, map, clusterManager) {

    private var mSumItems: Boolean = false

    override fun onBeforeClusterItemRendered(item: T, markerOptions: MarkerOptions) {
        markerOptions.icon(getBitmap(item, chooseIcon(item, false), chooseTextColor(item, false)))
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
        marker.setIcon(getBitmap(item, chooseIcon(item, isSelected), chooseTextColor(item, isSelected)))
    }

    @DrawableRes
    fun chooseIcon(item: T, isSelected: Boolean): Int {
        var icon = if (isSelected) R.drawable.ic_map_pin_selected else R.drawable.ic_map_pin

        if (item is ClusterItemWithDisabling && !item.isMarkerEnabled()) {
            icon = if (isSelected) R.drawable.ic_map_pin_coming_soon_selected
            else R.drawable.ic_map_pin_coming_soon
        }
        return icon
    }

    fun chooseTextColor(item: T, isSelected: Boolean): Int {
        var color = if (isSelected) R.color.evcs_white else R.color.evcs_secondary_700

        if (item is ClusterItemWithDisabling && !item.isMarkerEnabled()) {
            color = if (isSelected) R.color.evcs_white
            else R.color.evcs_gray_300
        }
        return mContext.resources.getColor(color)
    }

    override fun onBeforeClusterRendered(cluster: Cluster<T>, markerOptions: MarkerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions)
        var text = cluster.size.toString()
        if (mSumItems && cluster.items.first() is ClusterItemWithValue) {
            text = (cluster.items as Collection<ClusterItemWithValue>)
                .sumOf { item -> item.getMarkerValue() }.toString()
        }

        var icon = R.drawable.layout_oval_orange
        if (cluster.items.first() is ClusterItemWithDisabling) {
            if (!isClusterEnabled(cluster.items as Collection<ClusterItemWithDisabling>)) {
                icon = R.drawable.layout_oval_gray
            }
        }

        val bitmap = createMarker(text, icon, mContext.resources.getColor(R.color.evcs_secondary_700), Gravity.CENTER)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap))
    }

    private fun isClusterEnabled(items: Collection<ClusterItemWithDisabling>): Boolean {
        return items.fold(items.first().isMarkerEnabled()) { acc, item -> item.isClusterEnabled(acc) }
    }

    private fun getBitmap(item: T, @DrawableRes icon: Int, textColor: Int): BitmapDescriptor {
        if (item is ClusterItemWithText) {
            return BitmapDescriptorFactory.fromBitmap(createMarker(item.getMarkerText(), icon, textColor))
        } else {
            return BitmapDescriptorFactory.fromResource(icon)
        }
    }

    /**
     * Creates a Bitmap with the specified icon, text and text params for use as a marker
     */
    private fun createMarker(text: String, @DrawableRes icon: Int,
                             textColor: Int = Color.WHITE, textAlign: Int = Gravity.CENTER_HORIZONTAL): Bitmap {
        val markerLayout = buildLayout(text, icon, textAlign, textColor)
        return BitmapUtils.bitmapFromView(markerLayout)
    }

    /**
     * Creates a layout for the above method
     */
    private fun buildLayout(text: String, @DrawableRes icon: Int, textAlign: Int, textColor: Int): ViewGroup {
        val binding = MarkerLayoutBinding.inflate(LayoutInflater.from(mContext))
        val markerLayout = binding.root
        binding.markerImage.setImageResource(icon)
        binding.markerText.text = text
        binding.markerText.setTextColor(textColor)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        params.gravity = textAlign
        binding.markerText.layoutParams = params
        if (textAlign == Gravity.CENTER_HORIZONTAL) {
            val paddingTop = mContext
                    .resources.getDimension(R.dimen.spacing_medium).toInt() * 1.1f
            binding.markerText.setPadding(0, paddingTop.toInt(), 0 ,0)
        }
        return markerLayout
    }

    /**
     * @param sumItems: if true, clusters will show the sum of the value of every item (if they have one)
     * if false, clusters will show their item count.
     */
    fun setSumItems(sumItems: Boolean) {
        mSumItems = sumItems
    }

    override fun shouldRenderAsCluster(cluster: Cluster<T>): Boolean {
        return cluster.size > 1
    }

}
