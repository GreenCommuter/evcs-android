package org.evcs.android.model;

public class Car {
    public int id;
    String make;
    public String model;
    String imageUrl;

    @Override
    public String toString() {
        return model;
    }
}
