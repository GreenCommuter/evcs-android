package org.evcs.android.util.validator;

import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import android.text.TextUtils;

import org.evcs.android.BaseConfiguration;
import org.evcs.android.Configuration;

import java.util.regex.Pattern;

public class AlphanumericTextInputValidator extends AbstractTextInputValidator {

    private Pattern mPattern;

    /**
     * Constructor. Receives the {@link TextInputLayout} to validate.
     *
     * @param textInputField TextInputLayout to validate and watch
     */
    public AlphanumericTextInputValidator(@NonNull TextInputLayoutInterface textInputField) {
        super(textInputField);
        mPattern = Pattern.compile(BaseConfiguration.Validations.ALPHANUMERIC_REGEX);
    }

    @Override
    protected boolean validateField(@NonNull CharSequence content) {
        return !TextUtils.isEmpty(content) && mPattern.matcher(content).matches();
    }

}
