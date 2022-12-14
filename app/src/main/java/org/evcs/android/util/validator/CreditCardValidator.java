package org.evcs.android.util.validator;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

import org.evcs.android.util.StringUtils;

public class CreditCardValidator extends AbstractTextInputValidator {

    /**
     * Constructor. Receives the {@link TextInputLayout} to validate.
     *
     * @param textInputField {@link TextInputLayout} to validate and watch
     */
    public CreditCardValidator(@NonNull TextInputLayoutInterface textInputField) {
        super(textInputField);
    }

    @Override
    protected boolean validateField(@NonNull CharSequence content) {
//        if (CreditCardProvider.getProvider(content, true) != null) {
            return validateLuhnNumber(StringUtils.onlyNumbers(content));
//        }
//        return false;
    }

    private static boolean validateLuhnNumber(String num) {
        if (num.equals("")) return false;
        int nCheck = 0;
        int nDigit;
        boolean bEven = false;

        for (int i = num.length() - 1 ; i >= 0 ; i--) {
            nDigit = Integer.parseInt(String.valueOf(num.charAt(i)));

            if (bEven) {
                if ((nDigit *= 2) > 9) nDigit -= 9;
            }
            nCheck += nDigit;
            bEven = !bEven;
        }

        return (nCheck % 10) == 0;
    }

}

