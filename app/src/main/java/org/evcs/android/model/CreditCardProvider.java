package org.evcs.android.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import org.evcs.android.R;
import org.evcs.android.util.StringUtils;

import java.util.regex.Pattern;

public enum CreditCardProvider {

    @SerializedName(value = "american_express", alternate={"AmericanExpress"})
    AMERICAN_EXPRESS("^3[47][0-9]{13}$", "^3[47]", R.drawable.cc_american),
    @SerializedName(value="jcb", alternate={"JCB"})
    JCB("^(?:2131|1800|35[0-9]{3})[0-9]{11}$", "^(2131|1800|35)", R.drawable.cc_jcb),
    @SerializedName(value="visa", alternate={"Visa"})
    VISA("^4[0-9]{12}(?:[0-9]{3})?$", "^4", R.drawable.cc_visa),
    @SerializedName(value="discover", alternate={"Discover"})
    DISCOVER("^6(?:011|5[0-9]{2})[0-9]{12}$", "^6(?:011|5)", R.drawable.cc_discover),
    @SerializedName(value = "mastercard", alternate ={"MasterCard"})
    MASTERCARD("^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}$", "^(?:5[1-5]|222[1-9]|22[3-9]|2[3-6]|27[01]|2720)", R.drawable.cc_mastercard);

    public final String mKey;
    public final String mPossible;
    private final @DrawableRes int mImage;

    CreditCardProvider(String key, String possible, @DrawableRes int image) {
        mKey = key;
        mPossible = possible;
        mImage = image;
    }

    public static @Nullable CreditCardProvider getProvider(CharSequence key, boolean isComplete) {
        key = StringUtils.onlyNumbers(key);
        for (CreditCardProvider provider : CreditCardProvider.values()) {
            if (Pattern.compile(isComplete ? provider.mKey : provider.mPossible).matcher(key).find()) {
                return provider;
            }
        }
        return null;
    }

    public String getUri() {
        return String.format("https://assets.braintreegateway.com/payment_method_logo/%s.png", toString().toLowerCase());
    }

    public String toPrintableString() {
        return toString().replace("_", " ");
    }

    public @DrawableRes int getDrawable() {
        return mImage;
    }
}


