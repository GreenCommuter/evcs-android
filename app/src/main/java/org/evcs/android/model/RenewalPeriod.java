package org.evcs.android.model;

public enum RenewalPeriod { MONTH("mo"), YEAR("yr"), WEEK("wk");

    private final String mSmall;

    RenewalPeriod(String small) {
        mSmall = small;
    }

    String toAdverb() {
        return toString() + "ly";
    }

    String toSmall() {
        return mSmall;
    }
}
