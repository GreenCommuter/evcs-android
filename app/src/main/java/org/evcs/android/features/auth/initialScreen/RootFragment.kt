package org.evcs.android.features.auth.initialScreen

import android.graphics.Color
import android.view.View
import androidx.navigation.fragment.findNavController
import org.evcs.android.ui.fragment.ErrorFragment
import com.base.core.presenter.BasePresenter
import com.base.core.util.NavigationUtils
import org.evcs.android.R
import org.evcs.android.databinding.FragmentRootBinding
import org.evcs.android.features.main.MainActivity
import org.evcs.android.features.map.setStatusBarColor
import org.evcs.android.util.Extras

class RootFragment : ErrorFragment<BasePresenter<*>>() {
    private lateinit var mBinding: FragmentRootBinding

    override fun layout(): Int {
        return R.layout.fragment_root
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter(this)
    }

    override fun init() {
        setStatusBarColor(Color.TRANSPARENT)
    }

    override fun setUi(v: View) {
        mBinding = FragmentRootBinding.bind(v)
    }

    override fun setListeners() {
        mBinding.fragmentRootFindALocation.setOnClickListener {
            NavigationUtils.jumpTo(requireContext(), MainActivity::class.java,
                    NavigationUtils.IntentExtra(Extras.MainActivity.IS_BOTTOM, false))
        }
        mBinding.fragmentRootLogIn.setOnClickListener {
            findNavController().navigate(RootFragmentDirections.actionRootFragmentToSignInFragment())
        }
        mBinding.fragmentRootSignUp.setOnClickListener {
            findNavController().navigate(RootFragmentDirections.actionRootFragmentToRegisterFragment())
        }
    }
}