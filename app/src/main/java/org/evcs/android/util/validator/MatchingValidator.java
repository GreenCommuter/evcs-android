package org.evcs.android.util.validator;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;

import org.evcs.android.BaseConfiguration;
import org.evcs.android.EVCSApplication;
import org.evcs.android.R;

/**
 * A validator that checks if an EmptyTextInputLayout has the same text as another
 * {@link EmptyTextInputLayout}
 */
public class MatchingValidator extends AbstractTextInputValidator {

    private final TextInputLayoutInterface mMatches;

    /**
     * Constructor
     *
     * @param matcher The TextInputLayout which will show an error if they don't match
     * @param matches The TextInputLayout against which to check
     */
    public MatchingValidator(@NonNull TextInputLayoutInterface matcher, TextInputLayoutInterface matches) {
        super(matcher);
        mMatches = matches;
        mMatches.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //This method must be empty
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //This method must be empty
            }

            @Override
            public void afterTextChanged(Editable editable) {
                forceValidate();
            }
        });
    }

    @Override
    protected boolean validateField(@NonNull CharSequence content) {
        return mMatches.getText().toString().equals(content.toString());
    }

    @Override
    protected String getErrorString() {
        return "Passwords must match";
    }
}
