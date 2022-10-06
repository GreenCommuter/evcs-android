package org.evcs.android.features.serviceSelection;

import org.evcs.android.model.user.User;
import org.evcs.android.ui.view.shared.IErrorView;

public interface IChooseServiceView extends IErrorView {

    /**
     * Method called when there is an error getting the user from API.
     *
     * @param user User stored in shared preferences.
     */
    void onLoadError(User user);

}
