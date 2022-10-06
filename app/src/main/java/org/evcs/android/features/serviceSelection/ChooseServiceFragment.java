package org.evcs.android.features.serviceSelection;

import androidx.annotation.NonNull;

import com.base.core.util.ToastUtils;

import org.evcs.android.EVCSApplication;
import org.evcs.android.R;
import org.evcs.android.features.main.MainNavigationController;
import org.evcs.android.model.shared.RequestError;
import org.evcs.android.model.user.User;
import org.evcs.android.navigation.INavigationListener;
import org.evcs.android.ui.drawer.IToolbarView;
import org.evcs.android.ui.fragment.LoadingFragment;
import org.evcs.android.ui.view.shared.SelectionView;
import org.evcs.android.util.Extras;
import org.evcs.android.util.WordingUtils;

import butterknife.BindView;

public class ChooseServiceFragment extends LoadingFragment<ChooseServicePresenter>
        implements IChooseServiceView, IToolbarView {

    @BindView(R.id.choose_service_vanpooling_view) SelectionView mChooseServiceVanpoolingView;
    @BindView(R.id.choose_service_carsharing_view) SelectionView mChooseServiceCarSharingView;

    private @NonNull IChooseServiceListener mChooseServiceListener;
    private boolean mAlreadyLoaded;
    // I could fetch the vanpool only  if there is an active booking I'd rather avoid chaining the
    // requests, considering that this is the main screen.
    private boolean mOpenVanpoolOnRetrieve;

    public static ChooseServiceFragment newInstance() {
        return new ChooseServiceFragment();
    }

    @Override
    public int layout() {
        return R.layout.fragment_choose_service;
    }

    @Override
    public ChooseServicePresenter createPresenter() {
        return new ChooseServicePresenter(this, EVCSApplication.getInstance().getRetrofitServices());
    }

    @Override
    public void init() {
        mChooseServiceListener = MainNavigationController.getInstance();

        setDescriptions();

        if (!mAlreadyLoaded) {
            getPresenter().getLoggedUser();
        }
    }

    @Override
    public void populate() {
    }

    @Override
    public void onResume(){
        super.onResume();
        if (isOpeningApp()) {
        }
    }

    private void showTermsDialog() {
    }

    @Override
    public void showError(@NonNull RequestError requestError) {
        ToastUtils.show(requestError.getBody());
    }

    public void setDescriptions() {
        mChooseServiceVanpoolingView.setDescription(WordingUtils.getWording(
                getString(R.string.choose_service_vanpooling_description_wording_key)));
        mChooseServiceCarSharingView.setDescription(WordingUtils.getWording(
                getString(R.string.choose_service_carsharing_description_wording_key)));
    }

    public boolean isOpeningApp() {
        return getActivity().getIntent().getBooleanExtra(Extras.Root.OPENING_KEY, false);
    }

    @Override
    public void onLoadError(User user) {
        mAlreadyLoaded = true;
        hideProgressDialog();
    }

    @NonNull
    @Override
    public ToolbarState getToolbarState() {
        return ToolbarState.SHOW_HAMBURGER;
    }

    @Override
    public String getToolbarTitle() {
        return getString(R.string.choose_service_title);
    }

    /**
     * Listener for the ChooseService Fragment
     */
    public interface IChooseServiceListener extends INavigationListener {

        void onCarSharingSelected(boolean skipPlaceholder);

    }

}
