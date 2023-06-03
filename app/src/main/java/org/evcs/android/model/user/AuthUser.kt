package org.evcs.android.model.user

import com.google.gson.annotations.SerializedName

class AuthUser : User() {
    @SerializedName("jwt")
    var sessionToken: String? = null
}