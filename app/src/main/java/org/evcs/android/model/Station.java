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
}