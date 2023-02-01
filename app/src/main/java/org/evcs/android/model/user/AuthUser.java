package org.evcs.android.model.user;

public class AuthUser extends User {

    public static AuthUser TestAuthUser() {
        AuthUser a = new AuthUser();
        a.jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6NDcsIm5vb2RvZV9pZCI6IjQ3IiwiZXhwIjoxNjgxMjYxOTk4fQ.c23H2SSDgmZIfjYemwj4CYxzRnD_M3MFS3R_-6kepIE";
        return a;
    }

    private String jwt;

    public String getSessionToken() {
        return jwt;
    }
}
