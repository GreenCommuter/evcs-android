package org.evcs.android.activity.account

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.evcs.android.activity.BaseActivity2
import org.evcs.android.databinding.ActivityVehicleInfoBinding
import org.evcs.android.util.UserUtils

class VehicleInformationActivity : BaseActivity2() {
    private lateinit var mChangeUserResult: ActivityResultLauncher<Intent>
    private lateinit var mBinding: ActivityVehicleInfoBinding

    override fun init() {
        mChangeUserResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            populate()
        }
    }

    override fun inflate(layoutInflater: LayoutInflater): View {
        mBinding = ActivityVehicleInfoBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun populate() {
        val user = UserUtils.getLoggedUser()
        mBinding.fragmentAccountCar.setText((user.userCar ?: "").toString())
        mBinding.fragmentAccountZipcode.setText(user.zipCode)
    }

    override fun setListeners() {
        mBinding.fragmentAccountCar.setOnClickListener {
            mChangeUserResult.launch(Intent(this, ChangeCarActivity::class.java))
        }
        mBinding.fragmentAccountZipcode.setOnClickListener {
            mChangeUserResult.launch(Intent(this, ZipCodeActivity::class.java))
        }
    }

}