package org.evcs.android.model;

import org.evcs.android.util.StringUtils;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

public class Station implements Serializable {
    int id;
    String name;
    String availableStatus;
    float kw;
//    DateTime lastSynchedAt;
    List<Outlet> outlets;
    String chargerType;
    Pricing pricing;
    List<Connector> connectors;

    public int getId() {
        return id;
    }

    public String printKw() {
        return String.format("%.0fkW", kw);
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

    public CharSequence printAvailableStatus() {
        return StringUtils.capitalize(availableStatus.toLowerCase().replace("_", " "));
    }

    public boolean isAvailable() {
        return getAvailableStatus().equals(AvailableStatus.AVAILABLE);
    }

    public int getFirstConnectorId() {
        return connectors.get(0).id;
    }

    public enum AvailableStatus {
        AVAILABLE, BLOCKED, IN_USE, INOPERATIVE, UNDER_REPAIR, PLANNED, REMOVED, RESERVED, UNKNOWN;
    }
}