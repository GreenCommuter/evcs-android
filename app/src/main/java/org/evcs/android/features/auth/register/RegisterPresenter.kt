package org.evcs.android.features.auth.register

import com.base.networking.retrofit.RetrofitServices
import org.evcs.android.features.auth.AuthPresenter
import org.evcs.android.features.auth.AuthView
import org.evcs.android.model.user.UserRequestSignup
import org.evcs.android.network.service.UserService

/**
 * The RegisterPresenter class wraps methods and API calls to register a new user.
 *
 * @param viewInstance View to do the authentication callbacks.
 */
class RegisterPresenter(viewInstance: AuthView?, services: RetrofitServices?) :
    AuthPresenter<AuthView?>(viewInstance, services) {

    fun register(firstName: String, lastName: String, email: String, pass: String) {
        getService(UserService::class.java).register(UserRequestSignup(firstName, lastName, email, pass))
            .enqueue(getLoginCallback())
    }

}