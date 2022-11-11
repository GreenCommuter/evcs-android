
import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import org.evcs.android.R

class ClusterRender<T : ClusterItem>(context: Context, map: GoogleMap, clusterManager: ClusterManager<T>) :
    DefaultClusterRenderer<T>(context, map, clusterManager) {

    companion object {
        const val SELECTED_ALPHA = 1.0f
        const val UNSELECTED_ALPHA = 0.5f
    }

    override fun onBeforeClusterItemRendered(item: T, markerOptions: MarkerOptions) {
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin_unselected))
//        if (item.isSelected) {
//            markerOptions.alpha(SELECTED_ALPHA)
//        } else {
//            markerOptions.alpha(UNSELECTED_ALPHA)
//        }
    }

//    override fun getColor(clusterSize: Int): Int {
//        return ContextCompat.getColor(context, R.color.my_color)
//    }

    fun onClusterItemChange(item: T, marker: Marker, isSelected : Boolean) {
        if (isSelected) {
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin_selected))
        } else {
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin_unselected))
        }
    }
}
