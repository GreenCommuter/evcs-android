package org.evcs.android.features.profile.wallet;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.base.core.fragment.BaseFragment;
import com.base.core.presenter.BasePresenter;

import org.evcs.android.EVCSApplication;
import org.evcs.android.R;
import org.evcs.android.databinding.FragmentPaymentInformationBinding;
import org.evcs.android.model.shared.RequestError;
import org.evcs.android.navigation.INavigationListener;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class WalletFragment extends BaseFragment<BasePresenter>
        implements WalletHeaderView.WalletHeaderInterface, IWalletView {

    public static WalletFragment newInstance() {
        Bundle args = new Bundle();
        WalletFragment fragment = new WalletFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int layout() {
        return R.layout.fragment_payment_information;
    }

    @Override
    public WalletPresenter createPresenter() {
        return new WalletPresenter(this, EVCSApplication.getInstance().getRetrofitServices());
    }

    @Override
    public void init() {
    }

    @Override
    public void setUi(View v) {
        super.setUi(v);
        @NonNull FragmentPaymentInformationBinding binding = FragmentPaymentInformationBinding.bind(v);
        WalletHeaderView headerView = binding.walletHeaderView;
        headerView.setParent(this);
    }

    //    @Override
//    public void showDialog(WolmoDialogFragment dialog) {
//        dialog.show(getFragmentManager());
//    }

    @Override
    public void onAddPaymentMethodSelected() {
        findNavController(this)
                .navigate(WalletFragmentDirections.actionWalletFragmentToAddCreditCardFragment());
    }

    @Override
    public void showError(@NonNull RequestError requestError) {

    }

    public interface IPaymentMethodsFragmentListener extends INavigationListener {}
}
