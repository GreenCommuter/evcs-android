package org.evcs.android.util.validator;

import android.text.Editable;
import android.widget.EditText;

import androidx.annotation.Nullable;

public interface TextInputLayoutInterface {
    void setErrorEnabled(boolean enabled);

    void setEmptyError(boolean error);

    void setError(@Nullable CharSequence errorMessage);

    EditText getEditText();

    boolean isErrorEnabled();

    Editable getText();

    void requestDelayedFocus();
}
