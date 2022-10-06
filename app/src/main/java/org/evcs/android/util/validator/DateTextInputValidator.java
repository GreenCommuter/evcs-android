package org.evcs.android.util.validator;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

import org.joda.time.format.DateTimeFormatter;

public class DateTextInputValidator extends AbstractTextInputValidator {

    private final DateTimeFormatter mDateTimeFormatter;

    /**
     * Constructor. Receives the {@link TextInputLayout} to validate.
     *
     * @param textInputField TextInputLayout to validate and watch
     */
    public DateTextInputValidator(@NonNull TextInputLayoutInterface textInputField, DateTimeFormatter formatter) {
        super(textInputField);
        mDateTimeFormatter = formatter;
    }

    @Override
    protected boolean validateField(@NonNull CharSequence content) {
        if (!TextUtils.isEmpty(content)) {
            try {
                mDateTimeFormatter.parseLocalDate(content.toString());
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return false;
    }

}
