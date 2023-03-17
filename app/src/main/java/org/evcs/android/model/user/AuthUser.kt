package org.evcs.android.model.user

import com.google.gson.annotations.SerializedName

class AuthUser : User() {
    @SerializedName("jwt")
    var sessionToken: String? = null

    companion object {
        fun TestAuthUser(): AuthUser {
            val a = AuthUser()
            a.sessionToken = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6NDcsIm5vb2RvZV9pZCI6IjQ3IiwiZXhwIjoxNjgxMjYxOTk4fQ.c23H2SSDgmZIfjYemwj4CYxzRnD_M3MFS3R_-6kepIE"
            return a
        }
    }
}