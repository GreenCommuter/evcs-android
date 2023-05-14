package org.evcs.android.features.profile.wallet

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.base.core.fragment.BaseDialogFragment
import com.base.core.fragment.BaseFragment
import com.base.core.presenter.BasePresenter
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentWalletBinding
import org.evcs.android.model.PaymentMethod
import org.evcs.android.model.shared.RequestError
import org.evcs.android.navigation.INavigationListener
import org.evcs.android.ui.view.shared.EVCSToolbar2
import org.evcs.android.util.Extras

class WalletFragment : BaseFragment<BasePresenter<*>>(), WalletHeaderFragment.WalletHeaderInterface, IWalletView {

    companion object {
        fun newInstance(): WalletFragment {
            val args = Bundle()
            val fragment = WalletFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var mToolbar: EVCSToolbar2

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
        val walletHeaderFragment = WalletHeaderFragment.newInstance()
        walletHeaderFragment.setParent(this)
        fragmentManager?.beginTransaction()?.replace(R.id.wallet_header_view, walletHeaderFragment)?.commit()
        mToolbar = binding.walletToolbar
    }

    override fun setListeners() {
        mToolbar.setNavigationOnClickListener { activity?.finish() }
    }

    override fun onAddPaymentMethodSelected() {
        NavHostFragment.findNavController(this)
            .navigate(WalletFragmentDirections.actionWalletFragmentToAddCreditCardFragment())
    }

    override fun goToDetail(item: PaymentMethod) {
        var args = Bundle()
        args.putSerializable(Extras.CreditCard.CREDIT_CARD, item)
        NavHostFragment.findNavController(this).navigate(R.id.showCreditCardFragment, args)
    }

    override fun showError(requestError: RequestError) {}

}