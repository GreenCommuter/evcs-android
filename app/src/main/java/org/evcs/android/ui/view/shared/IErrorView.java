package org.evcs.android.ui.view.shared;

import androidx.annotation.NonNull;

import org.evcs.android.model.shared.RequestError;

/**
 * Allows displaying of an error, typically used for API request errors.
 */
// TODO: Utilize State render system.
public interface IErrorView {

    /**
     * Shows the corresponding error.
     */
    void showError(@NonNull RequestError requestError);
}
