package org.evcs.android.features.profile.sessioninformation

import android.view.View
import androidx.navigation.fragment.findNavController
import com.base.core.presenter.BasePresenter
import org.evcs.android.R
import org.evcs.android.databinding.FragmentReceiptPlanBinding
import org.evcs.android.features.shared.StandardTextFieldNoBorder
import org.evcs.android.model.Payment
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.ViewUtils.setMargins
import org.joda.time.format.DateTimeFormat

class ReceiptPlanFragment : ErrorFragment<BasePresenter<*>>() {

    private lateinit var mPayment: Payment
    private lateinit var mBinding: FragmentReceiptPlanBinding

    override fun layout(): Int {
        return R.layout.fragment_receipt_plan
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }

    override fun init() {
        mPayment = requireArguments().getSerializable(Extras.SessionInformationActivity.PAYMENT) as Payment
    }

    override fun setUi(v: View) {
        mBinding = FragmentReceiptPlanBinding.bind(v)
    }

    override fun populate() {
        val dateTimeFormatter = DateTimeFormat.forPattern(getString(R.string.app_date_format))

//        mBinding.sessionInformationChargingSiteSubtitle.text = "${period} Plan"
        mBinding.sessionInformationChargingSiteDate.text = dateTimeFormatter.print(mPayment.createdAt)

        mPayment.receipt.forEach { line ->
            val v = StandardTextFieldNoBorder(requireContext(), line.label, line.detail)
//            (v.layoutParams as LinearLayout.LayoutParams)
                v.setMargins(0, 0, 0, resources.getDimension(R.dimen.spacing_medium_extra).toInt())
            mBinding.receiptPlanLayout.addView(v)
        }
    }

    override fun setListeners() {
        mBinding.sessionInformationToolbar.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}