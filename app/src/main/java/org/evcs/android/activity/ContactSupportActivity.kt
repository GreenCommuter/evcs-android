package org.evcs.android.activity

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import com.base.core.permission.PermissionListener
import com.base.core.permission.PermissionManager
import com.google.android.gms.maps.model.LatLng
import org.evcs.android.BaseConfiguration
import org.evcs.android.R
import org.evcs.android.databinding.ActivityContactSupportBinding
import org.evcs.android.features.shared.EVCSDialogFragment
import org.evcs.android.util.Extras
import org.evcs.android.util.LocationUtils
import org.evcs.android.util.StorageUtils

class ContactSupportActivity : BaseActivity2() {

    private var mShowAddress: Boolean = false
    private val CALL_PERMISSION = "android.permission.CALL_PHONE"
    private val ADDRESS = LatLng(34.101098, -118.004768)

    private lateinit var mBinding: ActivityContactSupportBinding

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivityContactSupportBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun init() {
        mShowAddress = intent.getBooleanExtra(Extras.ContactSupportActivity.SHOW_ADDRESS, false)
    }

    override fun populate() {
        mBinding.contactSupportPhone.setText(BaseConfiguration.EVCSInformation.PHONE_NUMBER)
        mBinding.contactSupportAddress.isVisible = mShowAddress
        mBinding.contactSupportReport.isVisible = !mShowAddress
    }

    override fun setListeners() {
        mBinding.contactSupportPhone.setOnClickListener { goToCallUs() }
        mBinding.contactSupportReport.setOnClickListener {
            startActivity(WebViewActivity.buildIntent(this,
                    "Report An Issue", BaseConfiguration.WebViews.REQUEST_URL))
        }
        mBinding.contactSupportAddress.setOnClickListener {
            LocationUtils.launchGoogleMapsWithPin(this, ADDRESS)
        }
    }

    fun goToCallUs() {
        goToPhone(
            StorageUtils.getStringFromSharedPreferences(
            Extras.Configuration.PHONE_NUMBER,
            BaseConfiguration.EVCSInformation.PHONE_NUMBER))
    }

    fun goToPhone(phone: String) {
        PermissionManager.getInstance().requestPermission(this, object : PermissionListener() {
            override fun onPermissionsGranted() {
                super.onPermissionsGranted()
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phone")
                startActivity(intent)
            }

            override fun onPermissionsDenied(deniedPermissions: Array<String?>) {
                super.onPermissionsDenied(deniedPermissions)
            }
        }, CALL_PERMISSION)
    }

    fun showPostReportDialog() {
        EVCSDialogFragment.Builder()
            .setTitle("Thank you for reporting an issue!")
            .setSubtitle("Your issue has been reported to our support team. Someone will respond to you within 24-48 hours.")
            .addButton(getString(R.string.app_close), { dialog -> dialog.dismiss() },
                R.drawable.layout_corners_rounded_blue_outline, R.color.button_text_color_selector_blue_outline)
            .show(supportFragmentManager)
    }
}