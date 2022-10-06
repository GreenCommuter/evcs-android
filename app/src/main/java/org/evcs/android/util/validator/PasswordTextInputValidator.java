package org.evcs.android.util.validator;

import androidx.annotation.NonNull;

import android.text.TextUtils;

import org.evcs.android.BaseConfiguration;
import org.evcs.android.Configuration;

/**
 * Common validator to validate if the field's length is equals or greater than the given parameter.
 */
public class PasswordTextInputValidator extends AbstractTextInputValidator {

    /**
     * Constructor.
     *
     * @param textInputField Field to watch
     */
    public PasswordTextInputValidator(@NonNull TextInputLayoutInterface textInputField) {
        super(textInputField);
    }

    @Override
    protected boolean validateField(@NonNull CharSequence content) {
        return !TextUtils.isEmpty(content) &&
                content.length() >= BaseConfiguration.Validations.PASSWORD_MIN_LENGTH;
    }

}
