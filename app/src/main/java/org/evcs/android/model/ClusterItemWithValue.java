package org.evcs.android.model;

import com.google.maps.android.clustering.ClusterItem;

public interface ClusterItemWithValue extends ClusterItem {
    int getMarkerValue();
}
