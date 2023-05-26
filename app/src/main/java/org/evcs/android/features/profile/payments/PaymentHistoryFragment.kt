package org.evcs.android.features.profile.payments

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.model.Payment
import org.evcs.android.util.Extras

class PaymentHistoryFragment : PaginationFragment<Payment, PaymentHistoryPresenter, PaymentHistoryAdapter>(), PaginationView<Payment> {

    companion object {
        fun newInstance(): PaymentHistoryFragment {
            val args = Bundle()
            val fragment = PaymentHistoryFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun createPresenter(): PaymentHistoryPresenter {
        return PaymentHistoryPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setListeners() {
        super.setListeners()
        setItemClickListener { item ->
            if (item.chargeId != null) {
                findNavController().navigate(PaymentHistoryFragmentDirections.actionPaymentHistoryFragmentToSessionInformationFragment(item.chargeId))
            } else {
                val args = Bundle()
                args.putSerializable(Extras.SessionInformationActivity.PAYMENT, item)
                findNavController().navigate(R.id.receiptPlanFragment, args)
            }
        }
    }

    override fun showEmpty() {
        mSwipeRefreshLayout.isRefreshing = false
    }

    override fun createAdapter(): PaymentHistoryAdapter {
        return PaymentHistoryAdapter()
    }

    override fun getEmptyText(): String? {
        return null
    }

    override fun getToolbarTitle(): String {
        return "Payment history"
    }

}
