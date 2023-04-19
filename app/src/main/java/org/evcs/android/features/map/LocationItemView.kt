package org.evcs.android.features.map

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.base.core.fragment.BaseDialogFragment
import com.base.core.presenter.BasePresenter
import org.evcs.android.R
import org.evcs.android.databinding.AdapterMapCarouselItemBinding
import org.evcs.android.model.Location
import org.evcs.android.util.LocationUtils


class LocationItemView(location: Location) : BaseDialogFragment<BasePresenter<*>>() {

    private var location: Location? = location
    private lateinit var mBinding: AdapterMapCarouselItemBinding
    private var mLastY = 0

    override fun init() {}

    override fun layout(): Int {
        return R.layout.adapter_map_carousel_item
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = AdapterMapCarouselItemBinding.bind(v)
        if (location != null) setLocation(location!!)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setListeners() {
        super.setListeners()
        mBinding.mapItemFragmentScroll.setOnTouchListener { _, event ->
            Log.d(event.action.toString(), currentY().toString())
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    mLastY = currentY()
                    resize(mBinding.activityLocationPicture, mLastY / 2)
                }
                MotionEvent.ACTION_UP -> {
                    snap()
                }
                else -> {}
            }
            false
        }
        mBinding.activityLocationGo.setOnClickListener {
            LocationUtils.launchGoogleMapsWithPin(context, location!!.latLng)
        }
    }

    private fun snap() {
        if (currentY() > mLastY) {
            if (currentY() <= getMaxScroll()) {
                scroll(getMaxScroll())
                resize(mBinding.activityLocationPicture, getMaxScroll()/2)
            }
        } else {
            if (currentY() == 0) {
                dismiss()
            }
            else if (currentY() <= getMaxScroll()) {
                scroll(0)
                resize(mBinding.activityLocationPicture, 0)
            }
        }
    }

    private fun getMaxScroll(): Int {
        return (mBinding.mapItemFragmentEmpty.height - resources.getDimension(R.dimen.status_bar_height))
                .toInt()
    }

    private fun currentY(): Int {
        return mBinding.mapItemFragmentScroll.scrollY
    }

    private fun scroll(y : Int) {
        mBinding.mapItemFragmentScroll.post { mBinding.mapItemFragmentScroll.smoothScrollTo(0, y) }
    }

    /*static*/ fun resize(view: View, height: Int) {
        val params = view.layoutParams
        params.height = height.coerceAtMost(getMaxScroll()/2)
        view.layoutParams = params
    }

    fun setLocation(location: Location) {
        mBinding.activityLocationTitle.text = location.name
        mBinding.activityLocationAddress.text = location.address.toString()
        mBinding.activityLocationPicture.setImageURI(location.imageUrls?.get(0))
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }
}