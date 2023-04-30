package org.evcs.android.features.map.clustermap

import com.google.maps.android.clustering.ClusterItem

interface ClusterItemWithDisabling : ClusterItem {
    fun isMarkerEnabled(): Boolean

    //If another item is enabled/disabled, should the cluster that contains both be enabled?
    fun isClusterEnabled(other: Boolean): Boolean
}

interface ClusterItemWithValue : ClusterItem {
    fun getMarkerValue(): Int
}

interface ClusterItemWithText : ClusterItem {
    fun getMarkerText(): String
}