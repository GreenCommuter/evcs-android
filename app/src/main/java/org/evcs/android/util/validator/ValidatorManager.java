package org.evcs.android.util.validator;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

/**
 * Manager to keep track of validators and help when validating a huge number of fields.
 */
public final class ValidatorManager {

    private final Set<AbstractTextInputValidator> mValidatorSet;
    private OnAnyTextChangedListener mOnAnyTextChangedListener;

    /**
     * Constructor
     */
    public ValidatorManager() {
        mValidatorSet = new HashSet<>();
    }

    /**
     * Listener that will be called when some validator's field is updated. You cannot override this
     * method because it's the Validator main purpose.
     *
     * @param editable Reference to updated TextInputLayout
     */
    final void notifyTextChanged(TextInputLayoutInterface editable) {
        if (mOnAnyTextChangedListener != null) {
            mOnAnyTextChangedListener.onTextChanged(editable);
        }
    }

    /**
     * Adds and register a new validator inside the ValidatorManager.
     *
     * @param validator Validator to register
     */
    public void addValidator(@NonNull AbstractTextInputValidator validator) {
        mValidatorSet.add(validator);
        validator.registerManager(this);
        notifyTextChanged(validator.getTextInputLayout());
    }

    /**
     * Removes a {@link AbstractTextInputValidator} for this manager.
     *
     * @param validator Validator to remove
     * @return <b>true</b> if the validator was removed, false otherwise
     */
    public boolean removeValidator(@NonNull AbstractTextInputValidator validator) {
        validator.unregisterManager();
        return mValidatorSet.remove(validator);
    }

    /**
     * Checks whether all the validators contains valid value or not.
     *
     * @return True if all the fields inside the registered validators are valid.
     */
    public final boolean areAllFieldsValid() {
        for (AbstractTextInputValidator validator : mValidatorSet) {
            if (!validator.isValidField()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if at least one validator contains a valid value or not.
     *
     * @return True if at least one field inside the registered validators is valid.
     */
    public final boolean areSomeFieldsValid() {
        for (AbstractTextInputValidator validator : mValidatorSet) {
            if (validator.isValidField()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets a listener to get notified when any field is updated.
     *
     * @param listener OnAnyTextChangedListener
     */
    public void setOnAnyTextChangedListener(OnAnyTextChangedListener listener) {
        mOnAnyTextChangedListener = listener;
    }

    /**
     * Interface to get notified when a field is modified.
     */
    public interface OnAnyTextChangedListener {

        /**
         * Called when any text inside the validation manager is updated.
         *
         * @param inputLayout Input layout that contains the update text field.
         */
        void onTextChanged(TextInputLayoutInterface inputLayout);
    }

}
