package org.evcs.android.features.profile.payments

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.model.Payment

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
            val args = Bundle()
//            args.putSerializable(Extras.SessionInformationActivity.CHARGE, item)
            findNavController().navigate(R.id.sessionInformationFragment, args)
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
