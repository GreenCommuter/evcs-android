package org.evcs.android.features.map;

import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.algo.GridBasedAlgorithm;
import com.google.maps.android.clustering.algo.StaticCluster;

import java.util.HashSet;
import java.util.Set;

public class ZoomLimitedGridBasedAlgorithm<T extends ClusterItem> extends GridBasedAlgorithm<T> {

    private final int mZoomLimit;

    public ZoomLimitedGridBasedAlgorithm(int zoomLimit) {
        mZoomLimit = zoomLimit;
    }

    @Override
    public Set<? extends Cluster<T>> getClusters(double zoom) {
        if (zoom < mZoomLimit) return super.getClusters(zoom);
        HashSet<Cluster<T>> clusters = new HashSet();
        for (T t : getItems()) {
            StaticCluster<T> cluster = new StaticCluster<T>(t.getPosition());
            cluster.add(t);
            clusters.add(cluster);
        }
        return clusters;
    }
}
