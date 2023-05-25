package org.evcs.android.activity.subscription

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.activity.account.ChangePasswordView
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.UserService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils

class SubscriptionActivityPresenter(viewInstance: SubscriptionActivityView, services: RetrofitServices) :
        ServicesPresenter<SubscriptionActivityView?>(viewInstance, services) {

}