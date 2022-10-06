package org.evcs.android.util.validator;

import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import android.text.TextUtils;

/**
 * Common validator to validate if a field is empty or not.
 */
public class NonEmptyTextInputValidator extends AbstractTextInputValidator {

    /**
     * Constructor
     *
     * @param textInputField {@link TextInputLayout} to watch
     */
    public NonEmptyTextInputValidator(@NonNull TextInputLayoutInterface textInputField) {
        super(textInputField);
    }

    @Override
    protected boolean validateField(@NonNull CharSequence content) {
        return !TextUtils.isEmpty(content.toString().trim());
    }

}
