package org.evcs.android.util.validator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.evcs.android.features.shared.EmptyTextInputLayout;

public class RangeValidator extends AbstractTextInputValidator {
    private final Integer mMax;
    private final Integer mMin;

    public RangeValidator(EmptyTextInputLayout fieldToSet, @Nullable Integer min, @Nullable Integer max) {
        super(fieldToSet);
        mMin = min;
        mMax = max;
    }

    @Override
    protected boolean validateField(@NonNull CharSequence content) {
        int value;
        try {
            value = Integer.parseInt(content.toString());
        } catch (NumberFormatException e) {
            return false;
        }
        return (mMax == null || value <= mMax) && (mMin == null || value >= mMin);
    }
}
