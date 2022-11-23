package org.evcs.android.model;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

public class Station implements Serializable {
    int id;
    String name;
    String availableStatus;
    float kw;
    DateTime lastSynchedAt;
    List<Outlet> outlets;
    String chargerType;
    Pricing pricing;

    public int getId() {
        return id;
    }

    public float getKw() {
        return kw;
    }

    public List<Outlet> getOutlets() {
        return outlets;
    }

    public ConnectorType getChargerType() {
        for (ConnectorType ct : ConnectorType.values()) {
            if (ct.toString().equals(chargerType)) return ct;
        }
        return ConnectorType.TYPE1;
    }

    public Pricing getPricing() {
        return pricing;
    }

    public AvailableStatus getAvailableStatus() {
        for (AvailableStatus value : AvailableStatus.values()) {
            if (availableStatus.equals(value.toString())) {
                return value;
            }
        }
        return AvailableStatus.UNKNOWN;
    }

    public enum AvailableStatus {
        AVAILABLE, BLOCKED, CHARGING, INOPERATIVE, OUTOFORDER, PLANNED, REMOVED, RESERVED, UNKNOWN;
    }
}