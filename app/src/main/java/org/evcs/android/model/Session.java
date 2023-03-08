package org.evcs.android.model;

import org.joda.time.DateTime;

public class Session extends Charge {
//    "id":201,
//    "kwh":"0.0",
//    "status":"active",
    private DateTime updatedAt;
    private int stationName;
//duration

    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public int getStationName() {
        return stationName;
    }

}
