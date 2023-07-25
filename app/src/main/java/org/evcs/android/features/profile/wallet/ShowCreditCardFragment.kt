package org.evcs.android.features.profile.wallet

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.view.isVisible
import com.base.core.util.ToastUtils
import org.evcs.android.R
import org.evcs.android.features.shared.EVCSSliderDialogFragment
import org.evcs.android.model.CreditCard
import org.evcs.android.model.PaymentMethod
import org.evcs.android.model.shared.RequestError
import org.evcs.android.util.Extras
import org.evcs.android.util.UserUtils
import org.joda.time.DateTime

class ShowCreditCardFragment : AbstractCreditCardFragment(), AddCreditCardView {

    private var mPaymentMethod: PaymentMethod? = null

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
        mPaymentMethod = arguments?.getSerializable(Extras.CreditCard.CREDIT_CARD) as PaymentMethod?
        val cc = mPaymentMethod?.card
        if (cc == null) return

        mCreditCardView.setCreditCard(cc)
        mCardName.editText?.setText(mPaymentMethod!!.billingDetails?.name)
        mCardNumber.editText?.setText("•••• " + cc.last4!!)
        val exp = DateTime().withMonthOfYear(cc.expMonth).withYear(cc.expYear)
        mCardExpirationMonth.editText?.setText(mDateTimeFormatter.print(exp))
        mZipcode.editText?.setText("•••••")
        mCvv.editText?.setText("•••")
        setFields()
        mSetDefault.isVisible = true
        mNext.isEnabled = true //What if only card?
    }

    override fun setListeners() {
        super.setListeners()
        mSetDefault.isChecked = mPaymentMethod!!.id.equals(UserUtils.getLoggedUser().defaultPm)
        mSetDefault.isEnabled = !mSetDefault.isChecked
        mSetDefault.setOnCheckedClickListener { _, isChecked ->
            if (isChecked) {
                showProgressDialog()
                presenter.makeDefaultPaymentMethod(mPaymentMethod!!)
                mSetDefault.isEnabled = false
            }
        }
    }

    override fun onMakeDefaultError(error: RequestError) {
        super.showError(error)
        mSetDefault.isChecked = false
        mSetDefault.isEnabled = true
    }

    override fun onDefaultPaymentMethodSet(item: PaymentMethod) {
        hideProgressDialog()
        ToastUtils.show("Payment method set as default")
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
        EVCSSliderDialogFragment.Builder()
                .showDivider(true)
                .setTitle(getString(R.string.payment_method_dialog_remove_subtitle))
                .addButton(getString(R.string.payment_method_dialog_remove_button), { fragment ->
                    presenter.removePaymentMethod(mPaymentMethod!!)
                    fragment.dismiss()
                }, R.drawable.layout_corners_rounded_danger)
                .showCancel(true)
                .setCancelable(true)
                .show(requireFragmentManager())
    }

}