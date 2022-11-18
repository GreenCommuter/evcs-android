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

    public String getAvailableStatus() {
        return availableStatus;
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
}