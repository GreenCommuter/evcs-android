package org.evcs.android.model;

import java.io.Serializable;

public class Configuration implements Serializable {
    private int id;
    private String key;
    private String value;

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}