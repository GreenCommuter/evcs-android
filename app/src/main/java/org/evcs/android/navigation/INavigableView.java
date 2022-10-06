package org.evcs.android.navigation;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public interface INavigableView<T extends INavigationListener> {

    @NonNull
    Fragment setListener(T listener);

}
