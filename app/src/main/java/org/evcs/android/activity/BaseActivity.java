package org.evcs.android.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.base.core.fragment.IBaseFragment;
import com.base.core.permission.PermissionManager;
import com.base.core.util.ToastUtils;

import org.evcs.android.R;

import java.util.Iterator;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflate(getLayoutInflater());
        setContentView(view);
        if (handleArguments(getIntent().getExtras())) {
            setUi();
            init();
            populate();
            setListeners();
        } else {
            ToastUtils.show(R.string.unknown_error);
            finish();
        }

    }

    protected abstract View inflate(LayoutInflater layoutInflater);

    protected boolean handleArguments(Bundle args) {
        return true;
    }

    protected void setUi() {
    }

    protected abstract void init();

    protected void populate() {
    }

    protected void setListeners() {
    }

    protected void replaceFragment(int resId, Fragment f) {
        this.getSupportFragmentManager().beginTransaction().replace(resId, f).commit();
    }

    protected void replaceFragment(int resId, Fragment f, String tag) {
        this.getSupportFragmentManager().beginTransaction().replace(resId, f, tag).commit();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case 16908332:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @CallSuper
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @CallSuper
    public void onBackPressed() {
        List<Fragment> fragments = this.getSupportFragmentManager().getFragments();
        if (fragments != null) {
            Iterator var2 = fragments.iterator();

            while(var2.hasNext()) {
                Fragment childFragment = (Fragment)var2.next();
                if (childFragment instanceof IBaseFragment && childFragment.isVisible() && ((IBaseFragment)childFragment).onBackPressed()) {
                    return;
                }
            }
        }

        super.onBackPressed();
    }
}
