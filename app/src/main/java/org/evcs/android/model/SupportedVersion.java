package org.evcs.android.model;

public class SupportedVersion {

    private final boolean supported;
    private final String message;

    public SupportedVersion(boolean supported, String message) {
        this.supported = supported;
        this.message = message;
    }

    public boolean isSupported() {
        return supported;
    }

    public String getWording() { return message; }
}
