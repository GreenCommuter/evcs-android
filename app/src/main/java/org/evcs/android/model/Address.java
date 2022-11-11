package org.evcs.android.model;

import java.io.Serializable;

public class Address implements Serializable {
    String streetAddress;
    String city;
    String state;
    String zipCode;
    String country;

    @Override
    public String toString() {
        return streetAddress + ", " + city + ", " + state + " " + zipCode;
    }
}
