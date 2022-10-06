package org.evcs.android.model.shared;

import com.base.core.util.ContextUtils;

import org.evcs.android.R;

/**
 * Class that represents errors from failed requests. We can use the body to show the user a more
 * descriptive alert than just "network error"
 */
public class RequestError {
    private final String key;
    private final String title;
    private final String body;
    private final String type;

    public RequestError(String key, String title, String description, String type) {
        this.key = key;
        this.title = title;
        this.body = description;
        this.type = type;
    }

    public RequestError(String description) {
        this.key = null;
        this.title = "";
        this.body = description;
        this.type = null;
    }

    public static RequestError getNetworkError() {
        return new RequestError(ContextUtils.getAppContext().getString(R.string.app_error_network));
    }

    public static RequestError getUnknownError() {
        return new RequestError(ContextUtils.getAppContext().getString(R.string.unknown_error));
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getType() {
        return type;
    }

}
