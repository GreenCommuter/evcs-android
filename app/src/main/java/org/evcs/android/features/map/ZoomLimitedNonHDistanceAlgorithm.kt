package org.evcs.android.features.map

import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.algo.GridBasedAlgorithm
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm
import com.google.maps.android.clustering.algo.StaticCluster
import java.util.HashSet

class ZoomLimitedNonHDistanceAlgorithm<T : ClusterItem?>(private val mZoomLimit: Int) :
    NonHierarchicalDistanceBasedAlgorithm<T?>() {

    override fun getClusters(zoom: Double): Set<Cluster<T?>> {
        if (zoom < mZoomLimit) return super.getClusters(zoom)
        val clusters = HashSet<Cluster<T?>>()
        for (t in items) {
            val cluster = StaticCluster<T?>(t?.position)
            cluster.add(t)
            clusters.add(cluster)
        }
        return clusters
    }
}