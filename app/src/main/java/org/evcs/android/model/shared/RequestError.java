package org.evcs.android.model.shared;

import com.base.core.util.ContextUtils;

import org.evcs.android.R;

/**
 * Class that represents errors from failed requests. We can use the body to show the user a more
 * descriptive alert than just "network error"
 */
public class RequestError {
    private final String error;
    private final String errorMessage;

    public RequestError(String error, String errorMessage) {
        this.error = error;
        this.errorMessage = errorMessage;
    }

    public RequestError(String description) {
        this.errorMessage = description;
        this.error = null;
    }

    public static RequestError getNetworkError() {
        return new RequestError(ContextUtils.getAppContext().getString(R.string.app_error_network));
    }

    public static RequestError getUnknownError() {
        return new RequestError(ContextUtils.getAppContext().getString(R.string.unknown_error));
    }

    public String getBody() {
        return errorMessage;
    }

}
