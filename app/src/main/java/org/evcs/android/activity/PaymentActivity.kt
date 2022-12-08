package org.evcs.android.activity

import android.view.LayoutInflater
import android.view.View
import org.evcs.android.R
import org.evcs.android.databinding.ActivityPaymentBinding

class PaymentActivity : BaseActivity2() {

    private lateinit var mBinding: ActivityPaymentBinding

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivityPaymentBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun init() {}

    override fun populate() {
        super.populate()
        mBinding.fragmentPaymentToolbar.title = "Payment"
        mBinding.fragmentPaymentToolbar.navigationIcon = getDrawable(R.drawable.back_arrow)
    }
}