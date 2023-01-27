package org.evcs.android.features.profile.wallet

import com.base.core.fragment.BaseFragment
import com.base.core.presenter.BasePresenter
import org.evcs.android.features.profile.wallet.WalletHeaderView.WalletHeaderInterface
import org.evcs.android.R
import org.evcs.android.EVCSApplication
import androidx.navigation.fragment.NavHostFragment
import org.evcs.android.model.shared.RequestError
import org.evcs.android.navigation.INavigationListener
import android.os.Bundle
import android.view.View
import com.base.core.fragment.BaseDialogFragment
import org.evcs.android.databinding.FragmentWalletBinding
import org.evcs.android.ui.view.shared.EVCSToolbar

class WalletFragment : BaseFragment<BasePresenter<*>>(), WalletHeaderInterface, IWalletView {

    companion object {
        fun newInstance(): WalletFragment {
            val args = Bundle()
            val fragment = WalletFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var mToolbar: EVCSToolbar

    override fun layout(): Int {
        return R.layout.fragment_wallet
    }

    override fun createPresenter(): WalletPresenter {
        return WalletPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun init() {}

    override fun setUi(v: View) {
        super.setUi(v)
        val binding = FragmentWalletBinding.bind(v)
        val headerView = binding.walletHeaderView
        headerView.setParent(this)
        mToolbar = binding.walletToolbar
    }

    override fun setListeners() {
        mToolbar.setNavigationOnClickListener { activity?.finish() }
    }

    override fun showDialog(dialog: BaseDialogFragment<*>) {
        dialog.show(fragmentManager)
    }

    override fun onAddPaymentMethodSelected() {
        NavHostFragment.findNavController(this)
            .navigate(WalletFragmentDirections.actionWalletFragmentToAddCreditCardFragment())
    }

    override fun showError(requestError: RequestError) {}

    interface IPaymentMethodsFragmentListener : INavigationListener
}