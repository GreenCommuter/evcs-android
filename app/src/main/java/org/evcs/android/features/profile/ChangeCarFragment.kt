package org.evcs.android.features.profile

import androidx.core.view.isVisible
import org.evcs.android.features.auth.register.RegisterFragmentYourCar
import org.evcs.android.features.shared.DropdownWithLabel
import org.evcs.android.model.CarMaker
import org.evcs.android.model.user.UserCar
import org.evcs.android.util.UserUtils

/**
 * This used to be a separate fragment without zipcode and I was in a hurry
 */
class ChangeCarFragment : RegisterFragmentYourCar() {

    private var mUserCar: UserCar? = null

    override fun init() {
        super.init()
        mBinding.fragmentRegisterYourCarButton.text = "Save vehicle information"
        mBinding.fragmentRegisterYourCarSubtitle.isVisible = false
        mBinding.fragmentRegisterYouCarToolbar.setNavigationText("Back")
        mUserCar = UserUtils.getLoggedUser().userCar
    }

    override fun populate() {
        mBinding.fragmentRegisterYourCarMake.setItemAndSelect(mUserCar?.make)
        mBinding.fragmentRegisterYourCarYear.selectItem(mUserCar?.year)
        mBinding.fragmentRegisterYourCarZipcode.editText?.setText(UserUtils.getLoggedUser().zipCode)
    }

    //This is used to show the current selection while we retrieve the options
    fun DropdownWithLabel.setItemAndSelect(item : Any?) {
        if (item == null) return
        setItems(listOf(item))
        selectItem(item)
    }

    override fun showCarMakers(carMakers: List<CarMaker>) {
        super.showCarMakers(carMakers)
        mBinding.fragmentRegisterYourCarMake.selectItem(mUserCar?.make)
    }

    override fun onManufacturerClicked(manufacturer: String) {
        if (presenter.getCars(manufacturer).isEmpty()) {
            mBinding.fragmentRegisterYourCarModel.setItemAndSelect(mUserCar?.model)
        } else {
            super.onManufacturerClicked(manufacturer)
            mBinding.fragmentRegisterYourCarModel.selectItem(mUserCar?.model)
        }
    }

    override fun onCarsAdded(car : UserCar) {
        val user = UserUtils.getLoggedUser()
        user.userCar = car
        UserUtils.saveUser(user)
        activity?.finish()
    }

    override fun skipScreen(): Boolean {
        return false
    }

    override fun onBackPressed(): Boolean {
        requireActivity().finish()
        return true
    }

}