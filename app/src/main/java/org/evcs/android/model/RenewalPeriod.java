package org.evcs.android.model;

public enum RenewalPeriod { MONTH("mo"), YEAR("yr"), WEEK("week");

    private final String mSmall;

    RenewalPeriod(String small) {
        mSmall = small;
    }

    public String toAdverb() {
        return this + "ly";
    }

    public String toSmall() {
        return mSmall;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
