package org.evcs.android.features.profile.wallet

import org.evcs.android.ui.adapter.BaseRecyclerAdapter
import org.evcs.android.util.UserUtils
import android.view.ViewGroup
import android.view.LayoutInflater
import org.evcs.android.R
import org.evcs.android.model.PaymentMethod

class PaymentMethodAdapterV2 : BaseRecyclerAdapter<PaymentMethod, PaymentMethodViewHolder>() {

    private var mListener: CreditCardListener? = null
    private var mDefaultPm: String? = null

    init {
        mDefaultPm = UserUtils.getLoggedUser().defaultPm
    }

    override fun populate(holder: PaymentMethodViewHolder, item: PaymentMethod, position: Int) {
        holder.setPaymentMethod(item)
        holder.setDefault(item.id == mDefaultPm)
        holder.setListeners(object : PaymentMethodViewHolder.CreditCardViewListener {
            override fun onStarClicked() {
                if (mListener != null) mListener!!.onStarClicked(item)
            }

//            override fun onTrashClicked() {
//                if (mListener != null) mListener!!.onTrashClicked(item)
//            }

            override fun onDetailClicked() {
                if (mListener != null) mListener!!.onDetailClicked(item)
            }
        })
    }

    fun setOnItemClickListener(listener: CreditCardListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodViewHolder {
        val v = LayoutInflater
            .from(parent.context).inflate(R.layout.view_credit_card_item, parent, false)
        return PaymentMethodViewHolder(v)
    }

    fun setDefault(id: String) {
        mDefaultPm = id
        notifyDataSetChanged()
    }

    fun setDefault(item: PaymentMethod) {
        setDefault(item.id!!)
    }

    fun getDefault(): PaymentMethod? {
        //Ugly
        val items = IntRange(0, itemCount - 1).map { i -> get(i) }
        return items.firstOrNull { item -> item.id == mDefaultPm }
    }

    interface CreditCardListener {
        fun onStarClicked(item: PaymentMethod)
//        fun onTrashClicked(item: PaymentMethod)
        fun onDetailClicked(item: PaymentMethod)
    }
}