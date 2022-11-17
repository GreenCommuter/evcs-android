package org.evcs.android.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

class LocationHelper {

    private lateinit var mReceiver: FragmentLocationReceiver
    private lateinit var mLocationPermissionRequest: ActivityResultLauncher<Array<String>>
    private lateinit var mLocationSettingsRequest: ActivityResultLauncher<IntentSenderRequest>

    fun init(receiver : FragmentLocationReceiver) {
        mReceiver = receiver
        registerLocationPermission()
        registerLocationSettingsPermission()
        requestLocationPermission()
    }

    private fun registerLocationPermission() {
        mLocationPermissionRequest =
            mReceiver.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                permissions ->
                    when {
                        permissions[Manifest.permission.ACCESS_FINE_LOCATION]!! -> {
                            onLocationPermissionGranted()
                        }
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION]!! -> {
                            onLocationPermissionGranted()
                        } else -> {
                            mReceiver.onLocationNotRetrieved()
                        }
                    }
        }
    }

    private fun registerLocationSettingsPermission() {
        mLocationSettingsRequest =
            mReceiver.registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
                activityResult ->
                    if (activityResult.resultCode == Activity.RESULT_OK) {
                        onLocationSettingsEnabled()
                    } else {
                        mReceiver.onLocationNotRetrieved()
                    }
            }
    }

    private fun requestLocationPermission() {
        mLocationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    private fun onLocationPermissionGranted() {
//        mLocationRequest = LocationRequest.Builder(50000).setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY).build()
        val builder = LocationSettingsRequest.Builder()
//            .addLocationRequest(mLocationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(mReceiver.requireActivity())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            onLocationSettingsEnabled()
        }
        task.addOnFailureListener { exception ->
            try {
                val intentSenderRequest = IntentSenderRequest.Builder((exception as ApiException).status.resolution!!).build()
                mLocationSettingsRequest.launch(intentSenderRequest)
            } catch (e: Exception) {
                mReceiver.onLocationNotRetrieved()
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun onLocationSettingsEnabled() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(mReceiver.requireActivity())
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location == null) mReceiver.onLocationNotRetrieved() else mReceiver.onLocationResult(location)
        }
        .addOnFailureListener {
            mReceiver.onLocationNotRetrieved()
        }
    }
}

interface LocationReceiver {
    fun onLocationNotRetrieved()

    fun onLocationResult(location: android.location.Location)

}

interface FragmentLocationReceiver : LocationReceiver {
    fun <I, O> registerForActivityResult(contract: ActivityResultContract<I, O>,
        callback: ActivityResultCallback<O>): ActivityResultLauncher<I>

    fun requireActivity(): Activity
}
