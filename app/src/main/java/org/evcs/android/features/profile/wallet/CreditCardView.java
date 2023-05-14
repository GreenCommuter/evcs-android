package org.evcs.android.features.profile.wallet;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.drawee.view.SimpleDraweeView;

import org.evcs.android.databinding.ViewCreditCardBinding;
import org.evcs.android.model.CreditCard;
import org.evcs.android.model.CreditCardProvider;

public class CreditCardView extends LinearLayout {

    private TextView mLast4;
    private SimpleDraweeView mProvider;
    private TextView mName;
    private TextView mExpiration;
    private TextView mCode;

    public CreditCardView(Context context) {
        super(context);
        init(context);
    }

    public CreditCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CreditCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        @NonNull ViewCreditCardBinding binding = ViewCreditCardBinding.inflate(LayoutInflater.from(context), this, true);
        mLast4 = binding.creditCardViewLast4;
        mProvider = binding.creditCardViewProvider;
        mName = binding.creditCardViewName;
        mExpiration = binding.creditCardViewExpiration;
        mCode = binding.creditCardViewCvv;
        mLast4.setTypeface(Typeface.MONOSPACE);
    }

    public void setCreditCard(CreditCard creditCard) {
        mLast4.setText(creditCard.getLast4());
        if (creditCard.getBrand() != CreditCardProvider.UNKNOWN)
            mProvider.setImageResource(creditCard.getBrand().getLogo());
    }

    public void setName(String name) {
        mName.setText(name);
    }

    public void setExpiration(String expiration) {
        mExpiration.setText(expiration);
    }

    public void setCode(String code) {
        mCode.setText(code);
    }

    public void watchNumber(EditText number) {
        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mLast4.setText(s);
                CreditCardProvider provider = CreditCardProvider.Companion.getProvider(s, false);
                if (provider == null) {
                    mProvider.setImageDrawable(null);
                } else {
                    mProvider.setImageResource(provider.getLogo());
                }
            }
        });
    }

}
