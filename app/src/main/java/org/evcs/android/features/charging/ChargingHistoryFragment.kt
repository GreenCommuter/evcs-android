package org.evcs.android.features.charging

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.evcs.android.BaseConfiguration
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentChargingHistoryBinding
import org.evcs.android.model.Charge
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.ui.recycler.EndlessRecyclerView

class ChargingHistoryFragment : ErrorFragment<ChargingHistoryPresenter>(), ChargingHistoryView {

    private lateinit var mAdapter: ChargingHistoryAdapter
    private lateinit var mRecyclerView: EndlessRecyclerView

    companion object {
        fun newInstance(): ChargingHistoryFragment {
            val args = Bundle()
            val fragment = ChargingHistoryFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun layout(): Int {
        return R.layout.fragment_charging_history
    }

    override fun init() {
        val mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.setUp(true, { presenter.getNextPage() },
            BaseConfiguration.ChargingHistory.ITEMS_VISIBLE_THRESHOLD)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = mLayoutManager
    }

    override fun setUi(v: View) {
        super.setUi(v)
        val binding = FragmentChargingHistoryBinding.bind(v)
        mRecyclerView = binding.fragmentChargingHistoryRecycler
        mAdapter = ChargingHistoryAdapter()
    }

    override fun createPresenter(): ChargingHistoryPresenter {
        return ChargingHistoryPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun showEmpty() {
        //TODO: implement
    }

    override fun showCharges(chargesList: List<Charge?>, pagesLeft: Boolean, onFirstPage: Boolean) {
        if (onFirstPage) mAdapter.clear()
        mAdapter.appendBottomAll(chargesList)
//        mRecyclerView.toggleEndlessScrolling(pagesLeft)
        mRecyclerView.notifyLoadingFinished()
//        mSwipeRefreshLayout.setRefreshing(false)
//        hideProgressDialog()
    }

}
