package org.evcs.android.features.charging

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.evcs.android.BaseConfiguration
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentChargingHistoryBinding
import org.evcs.android.model.Charge
import org.evcs.android.ui.adapter.BaseRecyclerAdapterItemClickListener
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.ui.recycler.EndlessRecyclerView
import org.evcs.android.util.Extras

class ChargingHistoryFragment : ErrorFragment<ChargingHistoryPresenter>(), ChargingHistoryView {

    private lateinit var mAdapter: ChargingHistoryAdapter
    private lateinit var mRecyclerView: EndlessRecyclerView
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mEmptyView: TextView

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

    override fun setUi(v: View) {
        super.setUi(v)
        val binding = FragmentChargingHistoryBinding.bind(v)
        mRecyclerView = binding.fragmentChargingHistoryRecycler
        mSwipeRefreshLayout = binding.fragmentChargingHistorySwipeRefresh
        mEmptyView = binding.fragmentChargingHistoryEmptyView
        binding.fragmentChargingHistoryToolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
    }

    override fun init() {
        val mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mAdapter = ChargingHistoryAdapter()
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = mLayoutManager
    }

    override fun populate() {
        mSwipeRefreshLayout.isRefreshing = true
        mRecyclerView.setUp(true, { presenter.getNextPage() },
            BaseConfiguration.ChargingHistory.ITEMS_VISIBLE_THRESHOLD)
    }

    override fun setListeners() {
        mSwipeRefreshLayout.setOnRefreshListener { presenter?.reset() }
        mAdapter.setItemClickListener(object : BaseRecyclerAdapterItemClickListener<Charge>() {
            override fun onItemClicked(item: Charge, adapterPosition: Int) {
                val args = Bundle()
                args.putSerializable(Extras.SessionInformationActivity.CHARGE, item)
                findNavController().navigate(R.id.sessionInformationFragment, args)
            }
        })

    }

    override fun createPresenter(): ChargingHistoryPresenter {
        return ChargingHistoryPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun showEmpty() {
        mEmptyView.visibility = View.VISIBLE
        mRecyclerView.visibility = View.GONE
    }

    override fun showCharges(chargesList: List<Charge?>, pagesLeft: Boolean, onFirstPage: Boolean) {
        mEmptyView.visibility = View.GONE
        mRecyclerView.visibility = View.VISIBLE
        if (onFirstPage) mAdapter.clear()
        mAdapter.appendBottomAll(chargesList)
        mRecyclerView.toggleEndlessScrolling(pagesLeft)
        mRecyclerView.notifyLoadingFinished()
        mSwipeRefreshLayout.isRefreshing = false
        hideProgressDialog()
    }

    override fun onStart() {
        super.onStart()
        presenter.reset()
    }

}
