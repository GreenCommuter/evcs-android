package org.evcs.android.features.profile.wallet;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.drawee.view.SimpleDraweeView;

import org.evcs.android.R;
import org.evcs.android.databinding.ViewCreditCardBinding;
import org.evcs.android.model.CreditCard;
import org.evcs.android.model.CreditCardProvider;
import org.evcs.android.util.StringUtils;

public class CreditCardView extends LinearLayout {

    TextView mLast4;
    SimpleDraweeView mProvider;
    ImageView mStar;
    ImageView mTrash;

    private CreditCardViewListener mListener;

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
        mStar = binding.creditCardViewStar;
        mTrash = binding.creditCardViewTrash;
        mLast4.setTypeface(Typeface.MONOSPACE);

        mStar.setOnClickListener(view -> {
            if (mListener != null) mListener.onStarClicked();
        });
        mTrash.setOnClickListener(view -> {
            if (mListener != null) mListener.onTrashClicked();
        });
    }

    public void setCreditCard(CreditCard creditCard) {
        mLast4.setText(creditCard.last4);
        if (creditCard.getProvider() != null)
            mProvider.setImageResource(creditCard.getProvider().getDrawable());
        mStar.setImageResource(creditCard.isDefault() ? R.drawable.ic_star_filled : R.drawable.ic_star_filled);
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
                String numbers = StringUtils.onlyNumbers(s);
                mLast4.setText(numbers.subSequence(Math.min(12, numbers.length()), numbers.length()));
                CreditCardProvider provider = CreditCardProvider.getProvider(s, false);
                if (provider == null) {
                    mProvider.setImageDrawable(null);
                } else {
                    mProvider.setImageResource(provider.getDrawable());
                }
            }
        });
    }

    public void setListeners(CreditCardViewListener listener) {
        mListener = listener;
    }

    public void showButtons(boolean show) {
        mStar.setVisibility(show ? VISIBLE : GONE);
        mTrash.setVisibility(show ? VISIBLE : GONE);
    }

    public interface CreditCardViewListener {
        void onStarClicked();
        void onTrashClicked();
    }
}
