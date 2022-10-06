package org.evcs.android.util.validator;

import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 * This validator checks whether or not the field contains a string inside a collection of options.
 * This helps too validate if a field is within certain values.
 * The comparator is not case sensitive.
 */
public class OptionsTextInputValidator extends AbstractTextInputValidator {

    private final Set<String> mStringSet;

    /**
     * Constructor. Receives the {@link TextInputLayout} to validate.
     *
     * @param textInputField Text input to watch and validate
     * @param strings Collection of strings to check against, ignoring case
     */
    public OptionsTextInputValidator(@NonNull TextInputLayoutInterface textInputField,
                                     Collection<String> strings) {
        super(textInputField);
        mStringSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        mStringSet.addAll(strings);
    }

    @Override
    protected boolean validateField(@NonNull CharSequence content) {
       return mStringSet.contains(content.toString());
    }

}
