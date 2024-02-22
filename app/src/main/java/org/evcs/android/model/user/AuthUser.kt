package org.evcs.android.model.user

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

class AuthUser : User() {
    @SerializedName("jwt")
    var sessionToken: String? = null
    var expiresAt: DateTime? = null
}