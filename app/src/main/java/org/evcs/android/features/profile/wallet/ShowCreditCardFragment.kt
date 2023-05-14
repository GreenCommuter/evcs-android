package org.evcs.android.features.profile.wallet

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import org.evcs.android.R
import org.evcs.android.model.CreditCard
import org.evcs.android.model.PaymentMethod
import org.evcs.android.util.Extras

/**
 * Fragment that helps with the payment using BrainTree.
 * It shows a message and a popup to pay, and notifies its children when the payment was accepted.
 *
 * @param <P> Presenter extending [AddCreditCardPresenter]
</P> */
class ShowCreditCardFragment : AbstractCreditCardFragment(),
    AddCreditCardView {

    companion object {
        fun newInstance(cc: CreditCard): ShowCreditCardFragment {
            val args = Bundle()
            args.putSerializable(Extras.CreditCard.CREDIT_CARD, cc)
            val fragment = ShowCreditCardFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun populate() {
        val paymentMethod = arguments?.getSerializable(Extras.CreditCard.CREDIT_CARD) as PaymentMethod?
        val cc = paymentMethod?.card
        if (cc == null) return

        mCreditCardView.setCreditCard(cc)
        mCreditCardView.setName(paymentMethod.billingDetails?.name)
        mCardName.editText?.setText(paymentMethod.billingDetails?.name)
        mCardNumber.editText?.setText(cc.last4!!)
        mCardExpirationMonth.editText?.setText(cc.expMonth.toString() + "/" + cc.expYear.toString())
        mZipcode.editText?.setText("·····")
        mCvv.editText?.setText("···")
        mNext.isEnabled = true //What if only card?
    }

    override fun areFieldsEditable(): Boolean {
        return false
    }

    override fun getButtonText(): String {
        return "Remove Card"
    }

    override fun getButtonBackground(): Drawable {
        return resources.getDrawable(R.drawable.layout_corners_rounded_danger)
    }

    override fun onNextClicked() {
        //TODO: add slider dialog, do remove
    }

}