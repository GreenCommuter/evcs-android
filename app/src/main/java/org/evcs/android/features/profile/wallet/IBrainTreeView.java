package org.evcs.android.features.profile.wallet;

import org.evcs.android.ui.view.shared.IErrorView;

/**
 * View to listen to BrainTree events
 */
public interface IBrainTreeView extends IErrorView {

    /**
     * Returns the BrainTree token from the token endpoint to do BrainTree operations.
     *
     * @param token Token returned
     */
    void onTokenResponse(String token);

    /**
     * Method called when there was an error obtaining the BrainTree token.
     */
    void onTokenError();

    void onMakeDefaultFinished();
}
