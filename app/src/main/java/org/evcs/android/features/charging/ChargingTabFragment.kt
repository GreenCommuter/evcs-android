package org.evcs.android.features.charging

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import com.base.core.permission.PermissionListener
import com.base.core.permission.PermissionManager
import com.base.core.presenter.BasePresenter
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import org.evcs.android.R
import org.evcs.android.activity.ChargingActivity
import org.evcs.android.databinding.FragmentChargingTabBinding
import org.evcs.android.features.main.MainActivity
import org.evcs.android.features.main.MainNavigationController
import org.evcs.android.features.shared.StandardTextField
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras


class ChargingTabFragment : ErrorFragment<BasePresenter<*>>() {

    private lateinit var mLauncher: ActivityResultLauncher<Intent>
    lateinit var mSurfaceView: SurfaceView
    //    lateinit var txtBarcodeValue: TextView
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    private lateinit var mTextField: StandardTextField
    lateinit var mButton: TextView

    val mListener = MainNavigationController.getInstance()

    companion object {
        fun newInstance(): ChargingTabFragment {
            val args = Bundle()
            val fragment = ChargingTabFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun layout(): Int {
        return R.layout.fragment_charging_tab
    }

    override fun setUi(v: View) {
        super.setUi(v)
        val binding = FragmentChargingTabBinding.bind(v)
        mSurfaceView = binding.chargingTabSurfaceView
        mButton = binding.chargingTabButton
        mTextField = binding.chargingTabStationId
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }

    //Do this better
    override fun init() {
        mLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result -> if (result.resultCode != ChargingActivity.RESULT_CANCELED_WITH_DIALOG)
                MainNavigationController.getInstance().onMapClicked()
        }
    }

    override fun setListeners() {
        mButton.setOnClickListener {
            if (mTextField.editText!!.length() > 0) {
                goToPlanInfo(mTextField.editText!!.text.toString())
            }
        }
        mTextField.editText?.doAfterTextChanged {
            s -> mButton.isEnabled = !(s!!.isEmpty())
        }
    }

    private fun initialiseDetectorsAndSources() {
        Log.d("Barcode scanner", "started")
        barcodeDetector = BarcodeDetector.Builder(requireContext())
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()
        if (!barcodeDetector.isOperational) return
        cameraSource = CameraSource.Builder(requireContext(), barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true) //must
            .build()
        mSurfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                requestCameraPermission()
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                Log.d("Barcode scanner", "stopped")
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode?>) {
                val barcodes: SparseArray<Barcode?> = detections.detectedItems
                if (barcodes.size() != 0) {
                    val string = barcodes.valueAt(0)!!.displayValue
                    val uri = Uri.parse(string)
                    //check host
                    try {
                        val id = uri.getQueryParameter("id")
                        goToPlanInfo(id.toString(), true)
                    } catch (_ : java.lang.NullPointerException) {

                    }
                }
            }
        })
    }

    private fun goToPlanInfo(id: String, fromQR: Boolean = false) {
        val intent = Intent(requireContext(), ChargingActivity::class.java)
        intent.putExtra(Extras.PlanInfo.STATION_ID, id)
        intent.putExtra(Extras.PlanInfo.FROM_QR, fromQR)
        mLauncher.launch(intent)
    }

    @SuppressLint("MissingPermission")
    private fun requestCameraPermission() {
        PermissionManager.getInstance().requestPermission(this, object : PermissionListener() {
                override fun onPermissionsGranted() {
                    cameraSource.start(mSurfaceView.holder)
                }
            }, Manifest.permission.CAMERA)
    }

    override fun onPause() {
        super.onPause()
        if (::cameraSource.isInitialized)
            cameraSource.release()
    }

    override fun onResume() {
        super.onResume()
        initialiseDetectorsAndSources()
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).attachKeyboardListener()
    }

    override fun onStop() {
        (activity as MainActivity).detachKeyboardListener()
        super.onStop()
    }

    override fun onBackPressed(): Boolean {
        mListener.onMapClicked()
        return true;
    }
}
