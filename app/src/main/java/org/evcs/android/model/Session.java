package org.evcs.android.model;

import org.joda.time.DateTime;

import java.io.Serializable;

public class Session extends Charge implements Serializable {
    private int id;
//    "kwh":"0.0",
//    "status":"active",
    private DateTime updatedAt;
    private int stationName;
//duration

    public int getId() {
        return id;
    }

    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public int getStationName() {
        return stationName;
    }

}
