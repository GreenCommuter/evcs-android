package org.evcs.android.util.validator;

import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;

import org.evcs.android.BaseConfiguration;
import org.evcs.android.Configuration;

import java.util.regex.Pattern;

/**
 * Common validator to validate if a field is a valid phone.
 */
public class PhoneTextInputValidator extends AbstractTextInputValidator {

    private final Pattern mPhonePattern;

    /**
     * Constructor.
     *
     * @param textInputField {@link TextInputLayout} to watch
     */
    public PhoneTextInputValidator(@NonNull TextInputLayoutInterface textInputField) {
        super(textInputField);
        mPhonePattern = Pattern.compile(BaseConfiguration.Validations.PHONE_REGEX);
    }

    @Override
    protected boolean validateField(@NonNull CharSequence content) {
        return mPhonePattern.matcher(content).matches();
    }

}
