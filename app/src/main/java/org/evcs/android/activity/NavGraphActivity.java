package org.evcs.android.activity;

import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NavigationRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.base.core.fragment.IBaseFragment;

import org.evcs.android.R;

import java.util.List;

public abstract class NavGraphActivity extends BaseActivity2 {

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Navigation.findNavController(this, CONTAINER_VIEW_ID).setGraph(getNavGraphId());
    }

    protected abstract @NavigationRes int getNavGraphId();

    @Override
    public void onBackPressed() {
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.activity_base_content);
        List<Fragment> childFragments = navHostFragment == null ? null : navHostFragment.getChildFragmentManager().getFragments();
        for (Fragment childFragment : childFragments) {
            if (childFragment instanceof IBaseFragment && childFragment.isVisible() && ((IBaseFragment) childFragment).onBackPressed()) {
                return;
            }
        }
        super.onBackPressed();
    }
}
