package org.evcs.android.features.charging

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.features.profile.payments.PaginationFragment
import org.evcs.android.features.profile.payments.PaginationView
import org.evcs.android.model.Charge
import org.evcs.android.util.Extras

class ChargingHistoryFragment : PaginationFragment<Charge, ChargingHistoryPresenter, ChargingHistoryAdapter>(), PaginationView<Charge> {

    companion object {
        fun newInstance(): ChargingHistoryFragment {
            val args = Bundle()
            val fragment = ChargingHistoryFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setListeners() {
        super.setListeners()
        setItemClickListener { item ->
            findNavController().navigate(ChargingHistoryFragmentDirections.actionChargingHistoryFragmentToSessionInformationFragment(item.id))
        }
    }

    override fun createPresenter(): ChargingHistoryPresenter {
        return ChargingHistoryPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun getToolbarTitle(): String {
        return getString(R.string.profile_charge_history)
    }

    override fun createAdapter(): ChargingHistoryAdapter {
        return ChargingHistoryAdapter()
    }

    override fun getEmptyText(): String? {
        return getString(R.string.charging_history_empty)
    }

}
