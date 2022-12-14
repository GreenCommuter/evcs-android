package org.evcs.android.activity.payment

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.base.core.util.NavigationUtils
import com.base.core.util.ToastUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.databinding.ActivityPaymentBinding
import org.evcs.android.model.PaymentMethod
import org.evcs.android.model.shared.RequestError
import org.evcs.android.ui.view.shared.PaymentMethodView
import org.evcs.android.util.Extras

class PaymentActivity : BaseActivity2(), PaymentActivityView {

    private lateinit var mPresenter: PaymentActivityPresenter
    private lateinit var mBinding: ActivityPaymentBinding

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivityPaymentBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun init() {
        mPresenter = PaymentActivityPresenter(this, EVCSApplication.getInstance().retrofitServices)
        mPresenter.onViewCreated()
    }

    override fun populate() {
        mPresenter.getPaymentMethods()
    }

    override fun showPaymentMethods(response: List<PaymentMethod>?) {
        response?.forEach { paymentMethod ->
            val v = PaymentMethodView(paymentMethod, this)
            v.setOnClickListener {
                val intent = Intent(this, CardInformationActivity::class.java)
                intent.putExtra(Extras.PaymentActivity.CARD, paymentMethod.card)
                startActivity(intent)
            }
            mBinding.activityPaymentList.addView(v)
        }
    }

    override fun showError(requestError: RequestError) {
        ToastUtils.show(requestError.body)
    }

    override fun setListeners() {
        mBinding.fragmentPaymentToolbar.setNavigationOnClickListener { finish() }
        mBinding.activityPaymentAddCreditCard.setOnClickListener {
            NavigationUtils.jumpTo(this, AddCreditCardActivity::class.java)
        }
    }
}