package org.evcs.android.features.profile

import android.view.View
import android.widget.TextView
import org.evcs.android.EVCSApplication
import org.evcs.android.R
import org.evcs.android.databinding.FragmentChangeCarBinding
import org.evcs.android.features.auth.register.AbstractCarSelectionFragment
import org.evcs.android.features.auth.register.CarSelectionPresenter
import org.evcs.android.features.shared.DropdownWithLabel
import org.evcs.android.model.user.UserCar
import org.evcs.android.util.UserUtils

class ChangeCarFragment : AbstractCarSelectionFragment<CarSelectionPresenter<*>>() {

    private lateinit var mBinding: FragmentChangeCarBinding

    override fun setUi(v: View) {
        mBinding = FragmentChangeCarBinding.bind(v)
    }

    override fun getMakeField(): DropdownWithLabel {
        return mBinding.fragmentChangeCarMake
    }

    override fun getModelField(): DropdownWithLabel {
        return mBinding.fragmentChangeCarModel
    }

    override fun getYearField(): DropdownWithLabel {
        return mBinding.fragmentChangeCarYear
    }

    override fun getButton(): TextView {
        return mBinding.fragmentChangeCarSave
    }

    override fun layout(): Int {
        return R.layout.fragment_change_car
    }

    override fun createPresenter(): CarSelectionPresenter<*> {
        return CarSelectionPresenter(this, EVCSApplication.getInstance().retrofitServices)
    }

    override fun onCarsAdded(car : UserCar) {
        val user = UserUtils.getLoggedUser()
        user.userCar = car
        UserUtils.saveUser(user)
        activity?.finish()
    }

}