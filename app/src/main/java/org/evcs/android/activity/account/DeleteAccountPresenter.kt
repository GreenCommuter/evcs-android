package org.evcs.android.activity.account

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.UserService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.StorageUtils
import org.evcs.android.util.UserUtils

class DeleteAccountPresenter(viewInstance: DeleteAccountView, services: RetrofitServices) :
        ServicesPresenter<DeleteAccountView?>(viewInstance, services) {

    fun deleteAccount() {
        getService(UserService::class.java).deleteAccount()
                .enqueue(object : AuthCallback<Void>(this) {
                    override fun onResponseSuccessful(response: Void?) {
                        view.onAccountDeleted()
                        UserUtils.logout(null)
                        StorageUtils.clearKey(UserUtils.USER_EMAIL_PREF)
                    }

                    override fun onResponseFailed(responseBody: ResponseBody, code: Int) {
                        view.showError(ErrorUtils.getError(responseBody))
                    }

                    override fun onCallFailure(t: Throwable) {
                        view.showError(RequestError.getNetworkError())
                    }
                })
    }

}