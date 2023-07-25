package org.evcs.android.features.profile.wallet

import android.os.Bundle
import org.evcs.android.features.profile.wallet.PaymentMethodAdapterV2.CreditCardListener
import org.evcs.android.model.PaymentMethod

class ProfileWalletHeaderFragment : WalletHeaderFragment() {
    companion object {
        fun newInstance(): ProfileWalletHeaderFragment {
            val args = Bundle()
            val fragment = ProfileWalletHeaderFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getOnItemClickListener(): CreditCardListener {
        return object : CreditCardListener {
            override fun onDetailClicked(item: PaymentMethod) {
                mParent.goToDetail(item)
            }

            override fun onStarClicked(item: PaymentMethod) {
                mParent.goToDetail(item)
            }
        }
    }

    override fun showArrows(): Boolean {
        return true
    }
}