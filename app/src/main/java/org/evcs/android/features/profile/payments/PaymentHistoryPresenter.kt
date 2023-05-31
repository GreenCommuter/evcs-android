package org.evcs.android.features.profile.payments

import com.base.networking.retrofit.RetrofitServices
import org.evcs.android.model.PaginatedResponse
import org.evcs.android.model.Payment
import org.evcs.android.network.service.PaymentsService
import retrofit2.Call

class PaymentHistoryPresenter(viewInstance: PaginationView<Payment>, services: RetrofitServices) :
    PaginationPresenter<Payment, PaginationView<Payment>>(viewInstance, services) {

    override fun getCall(page: Int, perPage: Int): Call<PaginatedResponse<Payment>> {
        return getService(PaymentsService::class.java).getPayments(page, perPage)
    }

    override fun showEmpty() {
        return view.showEmpty()
    }

    override fun showItems(list: List<Payment>, pagesLeft: Boolean, onFirstPage: Boolean) {
        return view.showItems(list.filter { payment -> payment.amount > 0 }, pagesLeft, onFirstPage)
    }

}
