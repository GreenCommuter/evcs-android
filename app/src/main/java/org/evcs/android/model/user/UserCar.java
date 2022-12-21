package org.evcs.android.model.user;

import androidx.annotation.NonNull;

public class UserCar {
    private int carId;
    private String year;
    public String make;
    public String model;

    public UserCar(int carId, String year) {
        this.carId = carId;
        this.year = year;
    }

    @Override
    public @NonNull String toString() {
        return (year != null ? year + " " : "") + make + " " + model + " ";
    }
}
