package org.evcs.android.features.profile.wallet

import com.base.core.fragment.BaseFragment
import com.base.core.presenter.BasePresenter
import org.evcs.android.features.profile.wallet.WalletHeaderView.WalletHeaderInterface
import org.evcs.android.features.profile.wallet.IWalletView
import org.evcs.android.R
import org.evcs.android.features.profile.wallet.WalletPresenter
import org.evcs.android.EVCSApplication
import org.evcs.android.features.profile.wallet.WalletHeaderView
import androidx.navigation.fragment.NavHostFragment
import org.evcs.android.model.shared.RequestError
import org.evcs.android.navigation.INavigationListener
import org.evcs.android.features.profile.wallet.WalletFragment
import android.os.Bundle
import android.view.View
import org.evcs.android.databinding.FragmentPaymentInformationBinding

class WalletFragment : BaseFragment<BasePresenter<*>>(), WalletHeaderInterface, IWalletView {
    companion object {
        fun newInstance(): WalletFragment {
            val args = Bundle()
            val fragment = WalletFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun layout(): Int {
        return R.layout.fragment_payment_information
    }

    override fun createPresenter(): WalletPresenter {
        return WalletPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun init() {}
    override fun setUi(v: View) {
        super.setUi(v)
        val binding = FragmentPaymentInformationBinding.bind(v)
        val headerView = binding.walletHeaderView
        headerView.setParent(this)
    }

    //    @Override
    //    public void showDialog(WolmoDialogFragment dialog) {
    //        dialog.show(getFragmentManager());
    //    }
    override fun onAddPaymentMethodSelected() {
        NavHostFragment.findNavController(this)
            .navigate(WalletFragmentDirections.actionWalletFragmentToAddCreditCardFragment())
    }

    override fun showError(requestError: RequestError) {}

    interface IPaymentMethodsFragmentListener : INavigationListener
}