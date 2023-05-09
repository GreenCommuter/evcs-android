package org.evcs.android.features.subscriptions;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.drawee.view.SimpleDraweeView;

import org.evcs.android.databinding.LayoutImageCheckboxBinding;

/**
 * Horizontal {@link LinearLayout} containing a {@link CheckBox}, a toggleable
 * {@link SimpleDraweeView} and a {@link TextView}, in that order.
 */
public class ImageCheckBoxLayout extends LinearLayout {

    private CheckBox mCheckbox;
    private TextView mDescriptionText;

    public ImageCheckBoxLayout(Context context) {
        super(context);

        init(null, 0, 0);
    }

    public ImageCheckBoxLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs, 0, 0);
    }

    public ImageCheckBoxLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs, defStyleAttr, 0);
    }

    private void init(@Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOrientation(VERTICAL);

        @NonNull LayoutImageCheckboxBinding binding =
                LayoutImageCheckboxBinding.inflate(LayoutInflater.from(getContext()), this);


        mCheckbox = binding.imageCheckbox.getRoot();
        mDescriptionText = binding.imageCheckboxDescription;


        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mCheckbox.performClick();
            }
        });
    }
    /**
     * Sets whether the switch will be checked.
     *
     * @param checked Determines if it will be checked or not.
     */
    public final void setChecked(boolean checked) {
        mCheckbox.setChecked(checked);
    }

    /**
     * @return A boolean indicating current check state of the checkbox.
     */
    public final boolean isChecked() {
        return mCheckbox.isChecked();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        ((View) mCheckbox.getParent()).setPadding(left, top, right, bottom);
    }

    /**
     * Sets the text description.
     *
     * @param description Content of the description
     * @param font Text size of the description (default if null, takes first value)
     */
    public final void setDescription(@Nullable CharSequence description, float... font) {
        if (description == null) {
            mDescriptionText.setText(null);
            return;
        }
        mDescriptionText.setText(description);
        if (font.length > 0){
            mDescriptionText.setTextSize(TypedValue.COMPLEX_UNIT_PX, font[0]);
        }
    }

    /**
     * Sets the text description color.
     *
     * @param color Color size of the description (default if null, takes first value)
     */
    public final void setDescriptionColor(@ColorRes int color) {
        mDescriptionText.setTextColor(getResources().getColor(color));
    }

    public final void setSingleLine(boolean singleLine) {
        mDescriptionText.setSingleLine(singleLine);
    }

    /**
     * @return A string indicating current description of the checkbox.
     */
    public final String getDescription() {
        return (String) mCheckbox.getText();
    }


    public void setOnCheckedClickListener(CompoundButton.OnCheckedChangeListener listener) {
        mCheckbox.setOnCheckedChangeListener(listener);
    }
}
