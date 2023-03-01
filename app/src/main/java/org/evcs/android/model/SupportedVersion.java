package org.evcs.android.model;

public class SupportedVersion {

    private final boolean supported;
    private final String error;
    private final String errorMessage;

    public SupportedVersion(boolean supported, String message) {
        this.supported = supported;
        this.error = message;
        this.errorMessage = message;
    }

    public boolean isSupported() {
        return supported;
    }

    public String getWording() { return errorMessage; }
}
