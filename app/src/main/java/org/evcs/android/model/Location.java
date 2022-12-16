package org.evcs.android.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Location implements Serializable, ClusterItemWithText {
    public int id;
    public String name;
    public double latitude;
    public double longitude;
    public List<String> imageUrls;
/*        "station_count": {
        "dc50kw": 4,
                "dc": 0,
                "ac": 0
    },*/
    public Float distance;
    public List<Station> stations;
    public Address address;
//       "directions":"TO BE DEFINED",
//               "open_247":true,
//               "authority":"public",

//        "noodoe_id": "abc123",
//        "contact": {
//        "name": "Rina",
//        "title": "XS",
//        "email": "rina@sawayama.com",
//        "phone": "1 222 333 4444",
//        "company": "Sawayama"
//        },
//        "subscriptions_enabled": false

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return id == location.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    @Override
    public LatLng getPosition() {
        return getLatLng();
    }

    @Override
    public String getMarkerText() {
        return Integer.toString(stations.size());
    }

    public final CharSequence getPrintableDistance() {
        return (distance == null) ? "-- mi" : String.format("%.1f mi", distance);
    }
}
