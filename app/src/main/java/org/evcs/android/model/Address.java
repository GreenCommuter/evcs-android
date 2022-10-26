package org.evcs.android.model;

public class Address {
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
