package org.evcs.android.features.charging

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.BaseConfiguration
import org.evcs.android.model.Charge
import org.evcs.android.model.PaginatedResponse
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.ChargesService
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.PaginationState
import org.evcs.android.util.UserUtils

class ChargingHistoryPresenter(viewInstance: ChargingHistoryView?, services: RetrofitServices?) :
    ServicesPresenter<ChargingHistoryView>(viewInstance, services) {

    private var mState: PaginationState

    init {
        mState = PaginationState()
    }

    fun getNextPage() {
        getService(ChargesService::class.java)
            .getCharges(UserUtils.getUserId(), mState.page, BaseConfiguration.ChargingHistory.ITEMS_PER_PAGE)
            .enqueue(object : AuthCallback<PaginatedResponse<Charge?>>(this) {
                override fun onResponseSuccessful(response: PaginatedResponse<Charge?>) {
                    mState.updateTotal(response.totalPages)
                    val carBookingList: List<Charge?> = response.page
                    if (carBookingList.size == 0 && mState.isOnFirstPage) {
                        view.showEmpty()
                    } else {
                        view.showCharges(carBookingList, mState.pagesLeft(), mState.isOnFirstPage)
                        mState.advancePage()
                    }
                }

                override fun onResponseFailed(responseBody: ResponseBody, i: Int) {
                    view.showError(ErrorUtils.getError(responseBody))
                }

                override fun onCallFailure(throwable: Throwable?) {
                    runIfViewCreated(Runnable { view.showError(RequestError.getNetworkError()) })
                }
        })
    }

    fun reset() {
        mState = PaginationState()
        getNextPage()
    }
}
