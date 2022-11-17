package org.evcs.android.model;

import com.google.maps.android.clustering.ClusterItem;

public interface ClusterItemWithText extends ClusterItem {
    String getMarkerText();
}
