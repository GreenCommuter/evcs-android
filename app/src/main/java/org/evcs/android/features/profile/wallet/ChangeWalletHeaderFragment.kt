package org.evcs.android.features.profile.wallet

import android.os.Bundle
import org.evcs.android.features.profile.wallet.PaymentMethodAdapterV2.CreditCardListener
import org.evcs.android.model.PaymentMethod
import org.evcs.android.util.UserUtils

class ChangeWalletHeaderFragment : WalletHeaderFragment() {
    companion object {
        fun newInstance(): ChangeWalletHeaderFragment {
            val args = Bundle()
            val fragment = ChangeWalletHeaderFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getOnItemClickListener(): CreditCardListener {
        return object : CreditCardListener {
            override fun onDetailClicked(item: PaymentMethod) {}

            override fun onStarClicked(item: PaymentMethod) {
                if (item.id != UserUtils.getLoggedUser().defaultPm) {
                    showProgressDialog()
                    presenter.makeDefaultPaymentMethod(item)
                }
            }
        }
    }

    override fun showArrows(): Boolean {
        return false
    }
}