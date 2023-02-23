package org.evcs.android.features.charging

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.base.core.permission.PermissionListener
import com.base.core.permission.PermissionManager
import com.base.core.util.ToastUtils
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import org.evcs.android.R
import org.evcs.android.databinding.FragmentChargingTabBinding
import org.evcs.android.ui.fragment.ErrorFragment


class ChargingTabFragment : ErrorFragment<ChargingTabPresenter>(), ChargingTabView {

    lateinit var surfaceView: SurfaceView
    lateinit var txtBarcodeValue: TextView
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    lateinit var button: Button
    lateinit var intentData: String

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
        txtBarcodeValue = binding.chargingTabValue
        surfaceView = binding.chargingTabSurfaceView
        button = binding.chargingTabButton
    }

    override fun createPresenter(): ChargingTabPresenter {
        return ChargingTabPresenter(this, null)
    }

    override fun init() {}

    override fun setListeners() {
        button.setOnClickListener {
            if (intentData.length > 0) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(intentData)))
            }
        }
    }

    private fun initialiseDetectorsAndSources() {
        ToastUtils.show("Barcode scanner started")
        barcodeDetector = BarcodeDetector.Builder(requireContext())
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()
        cameraSource = CameraSource.Builder(requireContext(), barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true) //must
            .build()
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
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
                ToastUtils.show("Barcode scanner has been stopped")
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode?>) {
                val barcodes: SparseArray<Barcode?> = detections.detectedItems
                if (barcodes.size() != 0) {
//                    txtBarcodeValue.post {
                        button.text = "LAUNCH URL"
                        intentData = barcodes.valueAt(0)!!.displayValue
                        txtBarcodeValue.text = intentData
//                    }
                }
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun requestCameraPermission() {
        PermissionManager.getInstance().requestPermission(this@ChargingTabFragment,
            object : PermissionListener() {
                override fun onPermissionsGranted() {
                    cameraSource.start(surfaceView.holder)
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
