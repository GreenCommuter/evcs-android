package org.evcs.android.features.profile.wallet

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import androidx.annotation.CallSuper
import androidx.annotation.StyleRes
import androidx.navigation.fragment.NavHostFragment
import com.base.core.util.ToastUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentAddCreditCardBinding
import org.evcs.android.features.shared.StandardTextField
import org.evcs.android.features.subscriptions.ImageCheckBoxLayout
import org.evcs.android.model.PaymentMethod
import org.evcs.android.model.shared.RequestError
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.ViewUtils
import org.evcs.android.util.ViewUtils.setStyle
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

abstract class AbstractCreditCardFragment : ErrorFragment<AddCreditCardPresenter>(),
    AddCreditCardView {

    protected lateinit var mCardName: StandardTextField
    protected lateinit var mZipcode: StandardTextField
    protected lateinit var mCreditCardView: CreditCardView
    protected lateinit var mCardNumber: StandardTextField
    protected lateinit var mCardExpirationMonth: StandardTextField
    protected lateinit var mCvv: StandardTextField
    protected lateinit var mNext: Button
    protected lateinit var mSetDefault: ImageCheckBoxLayout

//    protected var listener: IBrainTreeListener? = null

    protected lateinit var mDateTimeFormatter: DateTimeFormatter

    override fun layout(): Int {
        return R.layout.fragment_add_credit_card
    }

    @CallSuper
    override fun init() {
        mCardNumber.editText!!.typeface = Typeface.MONOSPACE
        mDateTimeFormatter = DateTimeFormat.forPattern("MM/yy")
        mSetDefault.setDescription(getString(R.string.add_credit_card_set_default))
    }

    override fun createPresenter(): AddCreditCardPresenter {
        return AddCreditCardPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun setUi(v: View) {
        super.setUi(v)
        val binding = FragmentAddCreditCardBinding.bind(v)
        mCreditCardView = binding.fragmentBraintreeCreditCard
        mCardName = binding.fragmentBraintreeCardName
        mCardNumber = binding.fragmentBraintreeCardNumber
        mCardExpirationMonth = binding.fragmentBraintreeCardExpirationMonth
        mCvv = binding.fragmentBraintreeCardCvv
        mZipcode = binding.fragmentBraintreeCardZipcode
        mNext = binding.fragmentBraintreeNext
        mSetDefault = binding.fragmentAddCreditCardSetDefault
        binding.fragmentAddCreditCardToolbar.setTitle(getToolbarTitle())

        for (view in arrayListOf (mCardName, mCardNumber, mCardExpirationMonth, mCvv, mZipcode)) {
            view.isEnabled = areFieldsEditable()
        }

        ViewUtils.setAdjustResize(binding.fragmentAddCreditCardLayout)
    }

    abstract fun getToolbarTitle(): String

    fun setFields() {
        mCreditCardView.setName(mCardName.text.toString())
        mCreditCardView.setExpiration(mCardExpirationMonth.text.toString())
        mCreditCardView.setCode(mCvv.text.toString())
    }

    abstract fun areFieldsEditable(): Boolean

    @CallSuper
    override fun setListeners() {
        mNext.setOnClickListener { onNextClicked() }
        mNext.setStyle(getButtonStyle())
        mNext.text = getButtonText()
    }
    protected fun getDate() : LocalDate {
        return mDateTimeFormatter.parseLocalDate(mCardExpirationMonth.text.toString())
    }

    protected fun isDateValid(): Boolean {
        return getDate() != null && getDate().isAfter(LocalDate())
    }

    override fun showError(requestError: RequestError) {
        hideProgressDialog()
        ToastUtils.show(requestError.body)
    }

    abstract fun getButtonText(): String

    @StyleRes abstract fun getButtonStyle(): Int

    abstract fun onNextClicked()

    override fun onDefaultPaymentMethodSet(item: PaymentMethod) {
        hideProgressDialog()
        finish()
//        listener!!.onAddCreditCardFragmentFinished()
    }

    override fun onMakeDefaultError(error: RequestError) {
        showError(error)
    }

    override fun onPaymentMethodRemoved(item: PaymentMethod) {
        hideProgressDialog()
        finish()
    }

    /**
     * Method called when the [AbstractCreditCardFragment] finishes.
     */
    fun finish() {
        requireActivity().onBackPressed()
//        NavHostFragment.findNavController(this).popBackStack()
    }

//    interface IBrainTreeListener : INavigationListener {
//        fun onAddCreditCardFragmentFinished()
//    }
}