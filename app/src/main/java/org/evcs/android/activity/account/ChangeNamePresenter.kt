package org.evcs.android.activity.account

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.shared.RequestError
import org.evcs.android.model.user.NameWrapper
import org.evcs.android.model.user.User
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.UserService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.UserUtils

class ChangeNamePresenter(viewInstance: ChangeNameView?, services: RetrofitServices?) :
    ServicesPresenter<ChangeNameView?>(viewInstance, services) {

    fun changeName(text: CharSequence) {
        getService(UserService::class.java).updateUser(UserUtils.getUserId(), NameWrapper(text.toString()))
            .enqueue(object : AuthCallback<User?>(this) {
                override fun onResponseSuccessful(response: User?) {
                    UserUtils.saveUser(response)
                    view?.onNameUpdate();
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