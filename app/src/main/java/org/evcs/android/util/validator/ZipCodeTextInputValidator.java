package org.evcs.android.util.validator;

import androidx.annotation.NonNull;

import org.evcs.android.BaseConfiguration;
import org.evcs.android.Configuration;

import java.util.regex.Pattern;

/**
 * Common validator to validate if the a zip code is valid.
 */
public class ZipCodeTextInputValidator extends AbstractTextInputValidator {

    private final Pattern mZipCodePattern;

    /**
     * Constructor.
     *
     * @param textInputField Field to watch
     */
    public ZipCodeTextInputValidator(@NonNull TextInputLayoutInterface textInputField) {
        super(textInputField);
        mZipCodePattern = Pattern.compile(BaseConfiguration.Validations.ZIP_CODE_REGEX);
    }

    @Override
    protected boolean validateField(@NonNull CharSequence content) {
        return mZipCodePattern.matcher(content).matches();
    }

}
