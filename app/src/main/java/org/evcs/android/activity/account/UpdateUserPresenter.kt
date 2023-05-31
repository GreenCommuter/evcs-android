package org.evcs.android.activity.account

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.shared.RequestError
import org.evcs.android.model.user.*
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.UserService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.UserUtils

class UpdateUserPresenter(viewInstance: UpdateUserView?, services: RetrofitServices?) :
    ServicesPresenter<UpdateUserView?>(viewInstance, services) {

    fun changeName(first: CharSequence, last: CharSequence) {
        getService(UserService::class.java).updateUser(UserUtils.getUserId(), NameWrapper(first.toString(), last.toString()))
            .enqueue(getCallback())
    }

    private fun getCallback(): AuthCallback<User?> {
        return object : AuthCallback<User?>(this) {
            override fun onResponseSuccessful(response: User?) {
                UserUtils.saveUser(response)
                view?.onUserUpdate();
            }

            override fun onResponseFailed(responseBody: ResponseBody, i: Int) {
                view?.showError(ErrorUtils.getError(responseBody))
            }

            override fun onCallFailure(throwable: Throwable) {
                runIfViewCreated(Runnable { view?.showError(RequestError.getNetworkError()) })
            }
        }
    }

    fun changeZipCode(zipcode: CharSequence) {
        getService(UserService::class.java).updateUser(UserUtils.getUserId(), ZipCodeWrapper(zipcode.toString()))
            .enqueue(getCallback())
    }

    fun changePhoneNumber(phone: CharSequence) {
        getService(UserService::class.java).updateUser(UserUtils.getUserId(), PhoneWrapper(phone.toString()))
                .enqueue(getCallback())
    }

    fun changeEmail(email: CharSequence) {
        getService(UserService::class.java).updateUser(UserUtils.getUserId(), EmailWrapper(email.toString()))
                .enqueue(getCallback())

    }
}