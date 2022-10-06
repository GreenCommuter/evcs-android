package org.evcs.android.util.validator;

import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import android.text.TextUtils;

import org.evcs.android.BaseConfiguration;
import org.evcs.android.Configuration;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ExpirationTextInputValidator extends AbstractTextInputValidator {

    private final DateTimeFormatter mDateTimeFormatter;
    /**
     * Constructor. Receives the {@link TextInputLayout} to validate.
     *
     * @param textInputField TextInputLayout to validate and watch
     */
    public ExpirationTextInputValidator(@NonNull TextInputLayoutInterface textInputField) {
        super(textInputField);
        mDateTimeFormatter = DateTimeFormat.shortDate().withLocale(BaseConfiguration.DEFAULT_LOCALE);
    }

    public ExpirationTextInputValidator(@NonNull TextInputLayoutInterface textInputField, DateTimeFormatter formatter) {
        super(textInputField);
        mDateTimeFormatter = formatter;
    }

    @Override
    protected boolean validateField(@NonNull CharSequence content) {
        if (!TextUtils.isEmpty(content)) {
            try {
                return mDateTimeFormatter.parseLocalDate(content.toString()).isAfter(LocalDate.now());
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return false;
    }

}
