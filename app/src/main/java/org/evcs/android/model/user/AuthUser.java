package org.evcs.android.model.user;

public class AuthUser extends User {

    public static AuthUser TestAuthUser() {
        AuthUser a = new AuthUser();
        a.jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6NDMsIm5vb2RvZV9pZCI6IjQzIiwiZXhwIjoxNjcyMTcwMjIwfQ.so-2HoQOK50EDYILkHT3Bd6SV-YasQpyDDqZFhOEWMg";
        return a;
    }

    private String jwt;

    public String getSessionToken() {
        return jwt;
    }
}
