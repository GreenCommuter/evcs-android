package org.evcs.android.features.map

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.MeasureSpec
import com.base.core.fragment.BaseDialogFragment
import com.base.core.util.ToastUtils
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentLocationSliderBinding
import org.evcs.android.features.main.MainNavigationController
import org.evcs.android.features.map.location.ILocationView
import org.evcs.android.features.map.location.LocationPresenter
import org.evcs.android.model.Location
import org.evcs.android.model.shared.RequestError
import org.evcs.android.util.Extras

/** This is an extremely hacky component that mimics the behaviour of Instagram comments. It has an
 * outer scroll, with an empty view on top and at the bottom a space that is calculated to fill the
 * screen. Inside that space there is an inner scroll.
 */
class LocationSliderFragment : BaseDialogFragment<LocationPresenter>(),
    ILocationView {

    private lateinit var location: Location
    private var mMaxScroll: Int = 0
    private lateinit var mBinding: FragmentLocationSliderBinding
    private var mLastY = 0
    private var mLastAction: MotionEvent? = null

    companion object {
        fun newInstance(location: Location): LocationSliderFragment {
            val args = Bundle()
            args.putSerializable(Extras.LocationActivity.LOCATION, location)
            val fragment = LocationSliderFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun init() {
        presenter.onViewCreated()
        location = requireArguments().getSerializable(Extras.LocationActivity.LOCATION) as Location
        presenter.getLocation(location.id)
        mBinding.mapItemFragmentScroll.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
//        val height = mBinding.mapItemFragmentEmpty.measuredHeight
        val shadowPadding = 4

        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        val innerScrollInitialHeight = screenHeight -
                mBinding.fragmentLocationHandler.measuredHeight - shadowPadding
//        resizeViewHeight(mBinding.mapItemFragmentInnerScroll.parent as View, innerScrollInitialHeight)
        val emptyViewHeight = innerScrollInitialHeight - mBinding.mapItemFragmentLocationView.getMinVisibleHeight()
        resizeViewHeight(mBinding.mapItemFragmentEmpty, emptyViewHeight)

        mMaxScroll = emptyViewHeight - shadowPadding

        keepStatusBar(mBinding.root)
    }

    override fun showLocation(response: Location) {
        setLocation(response)
    }

    override fun showError(requestError: RequestError) {
        ToastUtils.show(requestError.body)
    }

    fun resizeViewHeight(v : View, height : Int) {
        v.layoutParams.height = height
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
        val onTouchListener = { v : View, event : MotionEvent ->
            Log.d(event.action.toString(), currentY().toString())
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    mLastY = currentY()
//                    resizePicture(mLastY / 2)
                }
                MotionEvent.ACTION_UP -> {
                    snap(v)
                }
            }
            mLastAction = MotionEvent.obtain(event)
            false
        }
        mBinding.mapItemFragmentScroll.setOnTouchListener(onTouchListener)
        mBinding.mapItemFragmentInnerScroll.setOnTouchListener(onTouchListener)
        //Touches on the empty part can be a click on the map.
        mBinding.mapItemFragmentEmpty.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    requireActivity().dispatchTouchEvent(event)
                }
                MotionEvent.ACTION_UP -> {
                    dismiss()
                    requireActivity().dispatchTouchEvent(event)
                }
            }
            mLastAction = MotionEvent.obtain(event)
            true
        }
    }

    //Magic
    /**
     * @param caller The view that was scrolled before this call
     */
    private fun snap(caller: View) {
        //Scroll upwards: snap at the top
        if (currentY() > mLastY) {
            if (currentY() <= getMaxScroll()) {
                scroll(getMaxScroll())
//                resizePicture(getMaxScroll()/2)
            }
        } else {
            //Scroll downwards. If the view was already at the bottom, both current and last equal 0
            //We will either dismiss the view (if it was already at the bottom)
            //Or snap at the bottom
            val innerScrollIsAtTop = mBinding.mapItemFragmentInnerScroll.scrollY == 0
            if (currentY() == 0 && (innerScrollIsAtTop || (caller == mBinding.mapItemFragmentScroll))
                && mLastAction?.action == MotionEvent.ACTION_MOVE) {
                dismiss()
            } else if (currentY() < mLastY && currentY() <= getMaxScroll()) {
                scroll(0)
//                resizePicture(0)
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
        mBinding.mapItemFragmentLocationView.addGoButton(location, requireFragmentManager())
        mBinding.mapItemFragmentLocationView.setStartChargingListener {
            dismiss()
            MainNavigationController.getInstance().goToPreCharging()
        }
    }

    override fun createPresenter(): LocationPresenter {
        return LocationPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

}