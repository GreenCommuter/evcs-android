package org.evcs.android.util.validator;

import android.util.Patterns;

import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;

public class EmailTextInputValidator extends AbstractTextInputValidator {

    /**
     * Constructor. Receives the {@link TextInputLayout} to validate.
     *
     * @param textInputField {@link TextInputLayout} to validate and watch
     */
    public EmailTextInputValidator(@NonNull TextInputLayoutInterface textInputField) {
        super(textInputField);
    }

    @Override
    protected boolean validateField(@NonNull CharSequence content) {
        return Patterns.EMAIL_ADDRESS.matcher(content).matches();
    }

}
