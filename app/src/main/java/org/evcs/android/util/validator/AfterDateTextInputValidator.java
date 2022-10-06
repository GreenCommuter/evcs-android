package org.evcs.android.util.validator;

import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import android.text.TextUtils;

import org.evcs.android.BaseConfiguration;
import org.evcs.android.Configuration;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * This validator checks whether the input text is after a given date or not.
 * If not compare date is set, the validator compares with {@link LocalDate#now()}
 */
public class AfterDateTextInputValidator extends AbstractTextInputValidator {

    private final DateTimeFormatter mDateTimeFormatter;
    private LocalDate mCompareDate;

    /**
     * Constructor. Receives the {@link TextInputLayout} to validate.
     *
     * @param textInputField TextInputLayout to validate and watch
     */
    public AfterDateTextInputValidator(@NonNull TextInputLayoutInterface textInputField) {
        super(textInputField);
        mDateTimeFormatter = DateTimeFormat.shortDate().withLocale(BaseConfiguration.DEFAULT_LOCALE);
    }

    @Override
    protected boolean validateField(@NonNull CharSequence content) {
        LocalDate compare = mCompareDate == null ? LocalDate.now() : mCompareDate;

        if (!TextUtils.isEmpty(content)) {
            try {
                return !mDateTimeFormatter.parseLocalDate(content.toString()).isBefore(compare);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Updates the comparison date for this validator
     *
     * @param compareDate New date to compare to
     */
    public void setCompareDate(@NonNull LocalDate compareDate) {
        mCompareDate = compareDate;
        forceValidate();
    }
}
