package org.evcs.android.features.auth.register

import okhttp3.ResponseBody
import org.evcs.android.model.shared.RequestError
import org.evcs.android.model.user.User
import org.evcs.android.model.user.ZipCodeWrapper
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.EVCSRetrofitServices
import org.evcs.android.network.service.UserService
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.UserUtils

class RegisterPresenterYourCar(viewInstance: RegisterViewYourCar, services: EVCSRetrofitServices) :
    CarSelectionPresenter<RegisterViewYourCar>(viewInstance, services) {

    fun updateZipcode(zipCode: String) {
        getService(UserService::class.java).updateUser(UserUtils.getUserId(), ZipCodeWrapper(zipCode))
            .enqueue(object : AuthCallback<User?>(this) {
                override fun onResponseSuccessful(response: User?) {
                    UserUtils.saveUser(response)
                    view?.onZipCodeUpdated();
                }

                override fun onResponseFailed(responseBody: ResponseBody, i: Int) {
                    view?.showError(ErrorUtils.getError(responseBody))
                }

                override fun onCallFailure(throwable: Throwable) {
                    runIfViewCreated(Runnable { view?.showError(RequestError.getNetworkError()) })
                }
            })
    }

}