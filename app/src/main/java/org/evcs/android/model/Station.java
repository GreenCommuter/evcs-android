package org.evcs.android.model;

import org.joda.time.DateTime;

import java.util.List;

public class Station {
    int id;
    String name;
    String availableStatus;
    float km;
    DateTime lastSynchedAt;
    List<Outlet> outlets;
    String chargerType;
    Pricing pricing;
}
