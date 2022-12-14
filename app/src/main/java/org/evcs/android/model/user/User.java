package org.evcs.android.model.user;

import org.joda.time.DateTime;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String email;
    public String firstName;
    public String lastName;
    public String phone;
    public UserCar userCar;
    private DateTime phoneVerifiedAt;
    private String zipCode;

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public boolean isPhoneVerified() {
        return phoneVerifiedAt != null;
    }

    public String getZipCode() {
        return zipCode;
    }
}
