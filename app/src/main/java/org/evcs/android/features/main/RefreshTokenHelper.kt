package org.evcs.android.features.main

import com.base.networking.retrofit.callback.NetworkCallback
import okhttp3.ResponseBody
import org.evcs.android.EVCSApplication
import org.evcs.android.model.user.AuthUser
import org.evcs.android.network.service.UserService
import org.evcs.android.util.UserUtils

class RefreshTokenHelper {
    companion object {
        fun onResume() {
            val expiration = UserUtils.getSessionTokenExpiration()
            if (expiration == null || expiration.minusDays(1).isBeforeNow) {
                refreshToken()
            }
        }

        fun refreshToken() {
            val service = EVCSApplication.getInstance().retrofitServices.getService(UserService::class.java)
            service.refreshToken().enqueue(object : NetworkCallback<AuthUser>() {
                override fun onResponseSuccessful(response: AuthUser) {
                    UserUtils.saveAuthUser(response)
                }

                override fun onResponseFailed(responseBody: ResponseBody, i: Int) {
                }

                override fun onCallFailure(throwable: Throwable) {
                }
            })
        }
    }
}
