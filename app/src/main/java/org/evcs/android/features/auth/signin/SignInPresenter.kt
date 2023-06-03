package org.evcs.android.features.auth.signin

import org.evcs.android.network.service.EVCSRetrofitServices
import org.evcs.android.features.auth.AuthPresenter
import org.evcs.android.features.auth.AuthView
import org.evcs.android.model.user.UserRequest
import org.evcs.android.network.service.UserService

/**
 * The SignInPresenter class wraps methods and API calls to authenticate an user.
 *
 * @param viewInstance View to do the authentication callbacks.
 */
class SignInPresenter(viewInstance: AuthView, evcsRetrofitServices: EVCSRetrofitServices?) :
    AuthPresenter<AuthView>(viewInstance, evcsRetrofitServices) {

    /**
     * Tries to log in with the given username and password.
     *
     * @param username Username to log in.
     * @param pass     Password to log in.
     */
    fun logIn(username: String, pass: String) {
        getService(UserService::class.java).logIn(UserRequest(username, pass))
            .enqueue(getLoginCallback())
    }

}