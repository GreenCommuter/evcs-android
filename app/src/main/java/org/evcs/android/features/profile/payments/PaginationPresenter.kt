package org.evcs.android.features.profile.payments

import com.base.networking.retrofit.RetrofitServices
import okhttp3.ResponseBody
import org.evcs.android.BaseConfiguration
import org.evcs.android.model.PaginatedResponse
import org.evcs.android.model.shared.RequestError
import org.evcs.android.network.callback.AuthCallback
import org.evcs.android.network.service.presenter.ServicesPresenter
import org.evcs.android.util.ErrorUtils
import org.evcs.android.util.PaginationState
import retrofit2.Call

abstract class PaginationPresenter<K, T : PaginationView<K>>(viewInstance: T, services: RetrofitServices) :
    ServicesPresenter<T>(viewInstance, services) {

    private var mState: PaginationState

    init {
        mState = PaginationState()
    }

    fun getNextPage() {
        getCall(mState.page, BaseConfiguration.ChargingHistory.ITEMS_PER_PAGE)
            .enqueue(object : AuthCallback<PaginatedResponse<K>>(this) {
                override fun onResponseSuccessful(response: PaginatedResponse<K>) {
                    mState.updateTotal(response.totalPages)
                    val list: List<K> = response.page!!
                    if (list.size == 0 && mState.isOnFirstPage) {
                        showEmpty()
                    } else {
                        showItems(list, mState.pagesLeft(), mState.isOnFirstPage)
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

    abstract fun showItems(list: List<K>, pagesLeft: Boolean, onFirstPage: Boolean)

    abstract fun showEmpty()

    abstract fun getCall(page: Int, perPage: Int): Call<PaginatedResponse<K>>

    fun reset() {
        mState = PaginationState()
    }
}