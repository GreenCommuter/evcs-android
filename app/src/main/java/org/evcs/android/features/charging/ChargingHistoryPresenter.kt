package org.evcs.android.features.charging

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.BaseConfiguration
import org.evcs.android.features.profile.payments.PaginationPresenter
import org.evcs.android.features.profile.payments.PaginationView
import org.evcs.android.model.Charge
import org.evcs.android.model.PaginatedResponse
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.ChargesService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.PaginationState
import org.evcs.android.util.UserUtils
import retrofit2.Call

class ChargingHistoryPresenter(viewInstance: PaginationView<Charge>, services: RetrofitServices) :
    PaginationPresenter<Charge, PaginationView<Charge>>(viewInstance, services) {

    override fun getCall(page: Int, perPage: Int): Call<PaginatedResponse<Charge>> {
        return getService(ChargesService::class.java).
            getCharges(UserUtils.getUserId(), page, perPage)
    }

    override fun showEmpty() {
        return view.showEmpty()
    }

    override fun showItems(list: List<Charge>, pagesLeft: Boolean, onFirstPage: Boolean) {
        return view.showItems(list, pagesLeft, onFirstPage)
    }

}
