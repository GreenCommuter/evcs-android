package org.evcs.android.activity;

import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NavigationRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.base.core.fragment.IBaseFragment;

import org.evcs.android.R;

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
        Fragment childFragment = navHostFragment == null ? null : navHostFragment.getChildFragmentManager().getFragments().get(0);
        if (childFragment instanceof IBaseFragment && childFragment.isVisible() && ((IBaseFragment) childFragment).onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}
