package org.evcs.android.features.account

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.UserService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils

class ChangePasswordPresenter(viewInstance: ChangePasswordView, services: RetrofitServices) :
    ServicesPresenter<ChangePasswordView?>(viewInstance, services) {

    private var mCallback: AuthCallback<Void?>
    private var mId: String? = null
    private var mEmail: String? = null

    init {
        mCallback = object : AuthCallback<Void?>(this) {
            override fun onResponseSuccessful(response: Void?) {
                view?.onPasswordChanged();
            }

            override fun onResponseFailed(responseBody: ResponseBody, i: Int) {
                view?.showError(ErrorUtils.getError(responseBody))
            }

            override fun onCallFailure(throwable: Throwable) {
                runIfViewCreated(Runnable { view?.showError(RequestError.getNetworkError()) })
            }
        }
    }

    fun onButtonClick(oldPassword: String, newPassword: String) {
        if (mEmail != null)
            changePassword(newPassword)
        else
            changePassword(oldPassword, newPassword)
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        getService(UserService::class.java).changePassword(oldPassword, newPassword, newPassword)
            .enqueue(mCallback)
    }

    fun changePassword(newPassword: String) {
        getService(UserService::class.java).changePassword(mEmail, mId, newPassword, newPassword)
                .enqueue(mCallback)
    }

    fun setParamsForReset(email: String, id: String) {
        mEmail = email;
        mId = id;
    }

}