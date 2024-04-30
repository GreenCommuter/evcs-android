package org.evcs.android.features.auth.register

import android.os.Bundle
import org.evcs.android.activity.NavGraphActivity
import android.view.LayoutInflater
import org.evcs.android.R
import android.view.View
import androidx.navigation.findNavController
import org.evcs.android.databinding.ActivityBaseNavhostBinding
import org.evcs.android.navigation.controller.AbstractNavigationController
import org.evcs.android.util.Extras
import java.io.Serializable

class VerifyPhoneActivity : NavGraphActivity() {

    enum class UseCase : Serializable {
        USER_REQUEST, AUTH, OUR_REQUEST
    }

    var mAskForCar: Boolean = false
    lateinit var mUseCase: UseCase

    override fun inflate(layoutInflater: LayoutInflater): View {
        return ActivityBaseNavhostBinding.inflate(layoutInflater).root
    }

    override fun init() {
        mUseCase = intent.getSerializableExtra(Extras.VerifyActivity.USE_CASE) as UseCase
        mAskForCar = intent.getBooleanExtra(Extras.VerifyActivity.ASK_FOR_CAR, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val destination : Int
        if (mAskForCar) {
            destination = R.id.registerFragmentYourCar
        } else {
            when (mUseCase) {
                UseCase.AUTH -> destination = R.id.registerFragmentYourCar
                UseCase.USER_REQUEST -> destination = R.id.changePhoneFragment
                UseCase.OUR_REQUEST -> destination = R.id.fragmentValidateOldUser
            }
        }
        val navOptions = AbstractNavigationController.replaceLastNavOptions(findNavController(containerViewId))
        findNavController(containerViewId).navigate(destination, null , navOptions)
    }

    override fun getNavGraphId(): Int {
        return R.navigation.navigation_verify
    }

    fun onVerifyFinished() {
        setResult(RESULT_OK)
        finish()
    }

    fun onCancel() {
        setResult(RESULT_CANCELED)
        finish()
    }

    fun onCellphoneSent(phone: String) {
        val args = Bundle()
        args.putString("previous_number", phone)
        findNavController(containerViewId).navigate(R.id.registerFragmentVerify)
    }
}