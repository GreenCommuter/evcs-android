package org.evcs.android.features.charging

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.base.core.permission.PermissionListener
import com.base.core.permission.PermissionManager
import com.base.core.util.ToastUtils
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import org.evcs.android.R
import org.evcs.android.databinding.FragmentChargingTabBinding
import org.evcs.android.features.main.MainNavigationController
import org.evcs.android.features.shared.StandardTextField
import org.evcs.android.ui.fragment.ErrorFragment
import java.net.URL


class ChargingTabFragment : ErrorFragment<ChargingTabPresenter>(), ChargingTabView {

    lateinit var mSurfaceView: SurfaceView
    //    lateinit var txtBarcodeValue: TextView
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    private lateinit var mTextField: StandardTextField
    lateinit var mButton: Button

    val mListener = ChargingNavigationController.getInstance()

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

    override fun createPresenter(): ChargingTabPresenter {
        return ChargingTabPresenter(this, null)
    }

    override fun init() {}

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
                    val id = uri.getQueryParameter("id")?.split(":")?.getOrNull(0)
                            ?.replace("[^0-9.]", "")
                    if (id?.toIntOrNull() != null) {
                        goToPlanInfo(id)
                    }
                }
            }
        })
    }

    private fun goToPlanInfo(id: String) {
        mListener.goToPlanInfo(id.toInt())
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
        cameraSource.release()
    }

    override fun onResume() {
        super.onResume()
        initialiseDetectorsAndSources()
    }
}
