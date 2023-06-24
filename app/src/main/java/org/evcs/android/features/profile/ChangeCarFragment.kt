package org.evcs.android.features.profile

import androidx.core.view.isVisible
import org.evcs.android.features.auth.register.RegisterFragmentYourCar
import org.evcs.android.model.user.UserCar
import org.evcs.android.util.UserUtils

/**
 * This used to be a separate fragment without zipcode and I was in a hurry
 */
class ChangeCarFragment : RegisterFragmentYourCar() {

    override fun init() {
        super.init()
        mBinding.fragmentRegisterYourCarToolbar.setNavigationOnClickListener { activity?.finish() }
        mBinding.fragmentRegisterYourCarButton.text = "Save vehicle information"
        mBinding.fragmentRegisterYourCarSubtitle.isVisible = false
    }

    override fun onCarsAdded(car : UserCar) {
        val user = UserUtils.getLoggedUser()
        user.userCar = car
        UserUtils.saveUser(user)
        activity?.finish()
    }

    override fun hasCompletedCarScreen(): Boolean {
        return false
    }

}