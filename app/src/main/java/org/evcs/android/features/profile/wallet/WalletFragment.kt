package org.evcs.android.features.profile.wallet

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.base.core.presenter.BasePresenter
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentWalletBinding
import org.evcs.android.model.PaymentMethod
import org.evcs.android.model.shared.RequestError
import org.evcs.android.navigation.controller.AbstractNavigationController
import org.evcs.android.ui.view.shared.EVCSToolbar2
import org.evcs.android.util.Extras
import org.evcs.android.ui.fragment.ErrorFragment

class WalletFragment : ErrorFragment<BasePresenter<*>>(), WalletHeaderFragment.WalletHeaderInterface, IWalletView {

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

    override fun onAddPaymentMethodSelected(clearStack: Boolean) {
        val navOptions = if (clearStack) AbstractNavigationController.replaceLastNavOptions(findNavController())
                         else null
        findNavController().
            navigate(WalletFragmentDirections.actionWalletFragmentToAddCreditCardFragment(), navOptions)
    }

    override fun goToDetail(item: PaymentMethod) {
        val args = Bundle()
        args.putSerializable(Extras.CreditCard.CREDIT_CARD, item)
        NavHostFragment.findNavController(this).navigate(R.id.showCreditCardFragment, args)
    }

    override fun showError(requestError: RequestError) {}

}