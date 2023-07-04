package org.evcs.android.features.profile.payments

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.evcs.android.BaseConfiguration
import org.evcs.android.R
import org.evcs.android.databinding.FragmentChargingHistoryBinding
import org.evcs.android.model.shared.RequestError
import org.evcs.android.ui.adapter.BaseRecyclerAdapter
import org.evcs.android.ui.adapter.BaseRecyclerAdapterItemClickListener
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.ui.recycler.EndlessRecyclerView

abstract class PaginationFragment<K, P : PaginationPresenter<K, *>, A : BaseRecyclerAdapter<K, *>>
    : ErrorFragment<P>(), PaginationView<K> {

    private lateinit var mAdapter: A
    private lateinit var mRecyclerView: EndlessRecyclerView
    protected lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mEmptyView: TextView

    override fun layout(): Int {
        return R.layout.fragment_charging_history
    }

    override fun setUi(v: View) {
        super.setUi(v)
        val binding = FragmentChargingHistoryBinding.bind(v)
        mRecyclerView = binding.fragmentChargingHistoryRecycler
        mSwipeRefreshLayout = binding.fragmentChargingHistorySwipeRefresh
        mEmptyView = binding.fragmentChargingHistoryEmptyView
        binding.fragmentChargingHistoryToolbar.setTitle(getToolbarTitle())
    }

    abstract fun getToolbarTitle(): String

    override fun init() {
        val mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mAdapter = createAdapter()
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = mLayoutManager
    }

    abstract fun createAdapter(): A

    override fun populate() {
        mSwipeRefreshLayout.isRefreshing = true
        presenter?.reset()
        mRecyclerView.setUp(true, { presenter?.getNextPage() },
            BaseConfiguration.ChargingHistory.ITEMS_VISIBLE_THRESHOLD)
        mEmptyView.text = getEmptyText()
    }

    abstract fun getEmptyText(): String?

    override fun setListeners() {
        mSwipeRefreshLayout.setOnRefreshListener {
            presenter?.reset()
            presenter?.getNextPage()
        }
    }

    fun setItemClickListener(listener: (K) -> Unit) {
        mAdapter.setItemClickListener(object : BaseRecyclerAdapterItemClickListener<K>() {
            override fun onItemClicked(item: K, adapterPosition: Int) {
                listener.invoke(item)
            }
        })
    }

    override fun showEmpty() {
        mEmptyView.visibility = View.VISIBLE
        mRecyclerView.visibility = View.GONE
        mSwipeRefreshLayout.isRefreshing = false
    }

    override fun showError(requestError: RequestError) {
        super.showError(requestError)
        mSwipeRefreshLayout.isRefreshing = false
    }

    override fun showItems(list: List<K?>, pagesLeft: Boolean, onFirstPage: Boolean) {
        mEmptyView.visibility = View.GONE
        mRecyclerView.visibility = View.VISIBLE
        if (onFirstPage) mAdapter.clear()
        mAdapter.appendBottomAll(list)
        mRecyclerView.toggleEndlessScrolling(pagesLeft)
        mRecyclerView.notifyLoadingFinished()
        mSwipeRefreshLayout.isRefreshing = false
        hideProgressDialog()
    }

}
