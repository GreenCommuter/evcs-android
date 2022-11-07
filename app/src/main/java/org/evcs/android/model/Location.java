package org.evcs.android.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

public class Location implements Serializable {
    public int id;
    public String name;
    public double latitude;
    public double longitude;
    List<String> imageUrls;
/*        "station_count": {
        "dc50kw": 4,
                "dc": 0,
                "ac": 0
    },*/
    public Float distance;
    List<Station> stations;
    public Address address;

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }
}
