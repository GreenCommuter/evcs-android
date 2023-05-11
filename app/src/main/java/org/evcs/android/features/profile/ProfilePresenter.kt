package org.evcs.android.features.profile

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.user.User
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.UserService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.UserUtils

open class ProfilePresenter(viewInstance: ProfileView?, services: RetrofitServices?)
    : ServicesPresenter<ProfileView>(viewInstance, services) {

    fun refreshUser() {
        getService(UserService::class.java).getCurrentUser().enqueue(
                object : AuthCallback<User>(this) {
                    override fun onResponseSuccessful(response: User) {
                        UserUtils.saveUser(response)
                        view.onUserRefreshed(response)
                    }

                    override fun onResponseFailed(responseBody: ResponseBody, code: Int) {}

                    override fun onCallFailure(t: Throwable) {}
                })
    }

}
