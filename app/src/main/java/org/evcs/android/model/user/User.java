package org.evcs.android.model.user;

import java.io.Serializable;

public class User implements Serializable {

    private int id;

    private String email;

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
