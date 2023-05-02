package org.evcs.android.features.map

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.MeasureSpec
import com.base.core.fragment.BaseDialogFragment
import com.base.core.presenter.BasePresenter
import org.evcs.android.R
import org.evcs.android.databinding.FragmentLocationSliderBinding
import org.evcs.android.features.main.MainNavigationController
import org.evcs.android.model.Location


class LocationSliderFragment(private var location: Location) : BaseDialogFragment<BasePresenter<*>>() {

    private var mMaxScroll: Int = 0
    private lateinit var mBinding: FragmentLocationSliderBinding
    private var mLastY = 0

    override fun init() {
        setLocation(location)
        mBinding.mapItemFragmentScroll.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        val height = mBinding.mapItemFragmentEmpty.measuredHeight
        mMaxScroll = height - resources.getDimension(R.dimen.status_bar_height).toInt()
        mBinding.mapItemFragmentScroll.setMinFling(mMaxScroll)
        keepStatusBar(mBinding.root)
    }

    override fun layout(): Int {
        return R.layout.fragment_location_slider
    }

    override fun setUi(v: View) {
        super.setUi(v)
        mBinding = FragmentLocationSliderBinding.bind(v)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setListeners() {
        super.setListeners()
        mBinding.mapItemFragmentScroll.setOnTouchListener { _, event ->
            Log.d(event.action.toString(), currentY().toString())
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    mLastY = currentY()
                    resizePicture(mLastY / 2)
                }
                MotionEvent.ACTION_UP -> {
                    snap()
                }
                else -> {}
            }
            false
        }
    }

    private fun snap() {
        if (currentY() > mLastY) {
            if (currentY() <= getMaxScroll()) {
                scroll(getMaxScroll())
                resizePicture(getMaxScroll()/2)
            }
        } else {
            if (currentY() == 0) {
                dismiss()
            }
            else if (currentY() <= getMaxScroll()) {
                scroll(0)
                resizePicture(0)
            }
        }
    }

    private fun getMaxScroll(): Int {
        return mMaxScroll
//        return (mBinding.mapItemFragmentEmpty.height - resources.getDimension(R.dimen.status_bar_height))
//                .toInt()
    }

    private fun currentY(): Int {
        return mBinding.mapItemFragmentScroll.scrollY
    }

    private fun scroll(y : Int) {
        mBinding.mapItemFragmentScroll.post { mBinding.mapItemFragmentScroll.smoothScrollTo(0, y) }
    }

    private fun resizePicture(height: Int) {
        mBinding.mapItemFragmentLocationView.resizePicture(height.coerceAtMost(getMaxScroll()/2))
    }

    fun setLocation(location: Location) {
        mBinding.mapItemFragmentLocationView.setLocation(location)
        mBinding.mapItemFragmentLocationView.setStartChargingListener {
            dismiss()
            MainNavigationController.getInstance().goToCharging()
        }
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }

}