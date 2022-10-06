package org.evcs.android.features.auth

import com.base.networking.retrofit.RetrofitServices
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.ui.view.shared.IErrorView

open class TermsPresenter<T>(viewInstance: T, services: RetrofitServices?) :
    ServicesPresenter<T>(viewInstance, services) where T : ITermsView?, T : IErrorView? {
    fun onTermsAccepted() {}
}