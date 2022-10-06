package org.evcs.android.util.validator;

import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

/**
 * Template to add easy field validators that validate and notify errors on {@link TextInputLayout}.
 * <b>NOTE:</b> This validator overwrites the {@link android.widget.EditText#setOnFocusChangeListener(View.OnFocusChangeListener)}
 * inside the {@link android.widget.EditText} watched. Use with caution if you need to add another
 * focus listener.
 */
public abstract class AbstractTextInputValidator {

    private ValidatorManager mValidatorManager;

    private final TextInputLayoutInterface mTextInputLayout;
    private boolean mNotifyErrorOnFocusLost = true;
    private boolean mNotifyEmptyField;
    private boolean mCleanErrorOnWrite;
    private boolean mIsValidField;

    private View.OnFocusChangeListener mOnFocusChangeListener;

    /**
     * Constructor. Receives the {@link TextInputLayout} to validate.
     *
     * @param textInputField {@link TextInputLayout} to validate and watch
     */
    public AbstractTextInputValidator(@NonNull TextInputLayoutInterface textInputField) {
        mTextInputLayout = textInputField;
        init();
    }

    /**
     * Add listeners and configure the validator
     */
    private void init() {
        if (mTextInputLayout.getEditText() == null) {
            throw new IllegalStateException("TextInputLayout must have an EditText.");
        }

        final View.OnFocusChangeListener oldListener = mTextInputLayout.getEditText().getOnFocusChangeListener();
        mTextInputLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                AbstractTextInputValidator.this.onFocusChange(view, focus);
                if (oldListener != null) oldListener.onFocusChange(view, focus);
            }
        });

        mTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //This method must be empty.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //This method must be empty.
            }

            @Override
            public void afterTextChanged(Editable editable) {
                forceValidate();
            }
        });
    }

    /**
     * Forces the validator to run the validations for this field and updates its state.
     */
    protected final void forceValidate() {
        EditText editText = mTextInputLayout.getEditText();

        mIsValidField = validateField(editText.getText());
        notifyManager(mTextInputLayout);

        if (editText.isFocused() && mCleanErrorOnWrite && mTextInputLayout.isErrorEnabled()
                && mTextInputLayout.getEditText().getText().length() > 0) {
            mTextInputLayout.setErrorEnabled(false);
            mTextInputLayout.setError(null);
        } else if (!editText.isFocused()) {
            onFocusChange(editText, mIsValidField);
        }
    }

    /**
     * Method called to track and update the {@link TextInputLayout} when the focus changes.
     *
     * @param view View to track
     * @param focus Has focus
     */
    private final void onFocusChange(View view, boolean focus) {
        if (focus && !mCleanErrorOnWrite) {
            mTextInputLayout.setErrorEnabled(false);
            mTextInputLayout.setError(null);
        } else if (getConditionToChange(focus)) {
                mTextInputLayout.setErrorEnabled(true);
                // Workaround to show a red line under the TextInputLayout
                // In the library, if the message is "null" or empty, the bottom line does
                // not get tinted red
                // So we need to append an empty space to set the line red, even if there is
                // no error message
                mTextInputLayout.setError(
                        TextUtils.isEmpty(getErrorString()) ? " " : getErrorString());
        }

        // TODO: Extract this logic to another class (observer) to avoid bloating this validator
        // Call external focus change listener
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener.onFocusChange(view, focus);
        }
    }

    private final boolean getConditionToChange (boolean focus) {
        return !focus &&
            mNotifyErrorOnFocusLost &&
            ((mNotifyEmptyField || mTextInputLayout.getEditText().length() > 0) && !isValidField());
    }

    /**
     * Configure the validator to show error when the field loses focus. <i>Default value:
     * true</i>
     *
     * @param notify Notify errors when focus is lost
     */
    public void setNotifyErrorOnFocusLost(boolean notify) {
        mNotifyErrorOnFocusLost = notify;
    }

    /**
     * Configure the validator to clean the error when te user writes on the field. Otherwise (when
     * it's false), the error is cleaned on focus. <i>Default value: false</i>
     *
     * @param cleanError Clean error when the user writes instead of on focus
     */
    public void setCleanErrorOnWrite(boolean cleanError) {
        mCleanErrorOnWrite = cleanError;
    }

    /**
     * Configure the validator to show error when the focus is lost on empty fields. <i>Default
     * value: false</i>
     *
     * @param notifyEmptyField Show error when a field is empty and losses focus.
     */
    public void setNotifyEmptyField(boolean notifyEmptyField) {
        mNotifyEmptyField = notifyEmptyField;
    }

    /**
     * Register a new manager to notify input events. This method is called when the validator is
     * added to a ValidatorManager. This method cannot be overridden.
     *
     * @param manager ValidatorManager reference.
     */
    final void registerManager(ValidatorManager manager) {
        mValidatorManager = manager;
    }

    /**
     * Unregister the manager for this validator
     */
    final void unregisterManager() {
        mValidatorManager = null;
    }

    /**
     * Notify manager when the field changes
     *
     * @param inputLayout Field reference
     */
    private void notifyManager(TextInputLayoutInterface inputLayout) {
        if (mValidatorManager != null) {
            mValidatorManager.notifyTextChanged(inputLayout);
        }
    }

    /**
     * Returns if the field is in a valid state or not.
     *
     * @return True if the field is in a valid state. False otherwise.
     */
    public boolean isValidField() {
        return mIsValidField;
    }

    /**
     * Sets an external FocusChangeListener for the edit text. The Validator overrides the {@link
     * View#setOnFocusChangeListener(View.OnFocusChangeListener)} for the {@link
     * android.widget.EditText}, this allows to add an extra listener to get notified. This listener
     * will be called <i>after</i> the validator get executed.
     *
     * @param onFocusChangeListener Listener to add
     */
    public void setEditTextOnFocusChangeListener(View.OnFocusChangeListener onFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener;
    }

    /**
     * Returns the {@link TextInputLayout} watched by this validator.
     *
     * @return {@link TextInputLayout} watched
     */
    @NonNull
    public TextInputLayoutInterface getTextInputLayout() {
        return mTextInputLayout;
    }

    /**
     * Validate the content of the field. This method must be implemented to add the required
     * validations and is called whenever the text changes.
     *
     * @param content Field's content
     * @return True if the field contains valid text, false otherwise.
     */
    protected abstract boolean validateField(@NonNull CharSequence content);

    /**
     * Returns the error message to show when the text inside the field is invalid.
     *
     * @return Error message to show
     */
    protected String getErrorString() {
        return null;
    }

}
