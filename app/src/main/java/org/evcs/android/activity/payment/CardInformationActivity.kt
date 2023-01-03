package org.evcs.android.activity.payment

import org.evcs.android.activity.BaseActivity2
import android.view.LayoutInflater
import android.view.View
import org.evcs.android.EVCSApplication
import org.evcs.android.databinding.ActivityCardInformationBinding
import org.evcs.android.model.Card
import org.evcs.android.network.service.EVCSRetrofitServices
import org.evcs.android.util.Extras
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.io.Serializable
import java.time.LocalDate

class CardInformationActivity : BaseActivity2(), CardInformationActivityView {

    private lateinit var mDateTimeFormatter: DateTimeFormatter
    private lateinit var mPresenter: CardInformationActivityPresenter
    private lateinit var mBinding: ActivityCardInformationBinding
    private lateinit var mCard: Card

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivityCardInformationBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun init() {
        mDateTimeFormatter = DateTimeFormat.forPattern("MM/yy")
        mPresenter = CardInformationActivityPresenter(this, EVCSApplication.getInstance().retrofitServices)
        mPresenter.onViewCreated()
        mCard = intent.getSerializableExtra(Extras.PaymentActivity.CARD) as Card
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.activityCardInformationToolbar.setNavigationOnClickListener { finish() }
        mBinding.fragmentCardInformationNumber.text = mCard.last4
        val date = DateTime.now().withMonthOfYear(mCard.expL).withYear(mCard.expYear)
        mBinding.fragmentCardInformationExpiration.text = mDateTimeFormatter.print(date)
        mBinding.activityCardInformationRemove.setOnClickListener { mPresenter.removeCard(mCard) }
    }
}