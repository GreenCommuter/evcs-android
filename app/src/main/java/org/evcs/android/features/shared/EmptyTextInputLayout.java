package org.evcs.android.features.shared;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

import org.evcs.android.R;
import org.evcs.android.util.validator.TextInputLayoutInterface;

import java.lang.reflect.Field;

public class EmptyTextInputLayout extends TextInputLayout implements TextInputLayoutInterface {

    private static final String TAG = "EmptyTextInputLayout";
    private static final int[] EMPTY_TEXT_STATE = new int[]{R.attr.state_empty_text};
    private static final int[] ERROR_TEXT_STATE = new int[]{R.attr.state_error_text};

    private boolean mEmptyText = true;
    private boolean mErrorActive;
    //private boolean mEnabled = true;

    private LinearLayout mIndicator;
    private EditText mEditText;


    public EmptyTextInputLayout(Context context) {
        super(context);
    }

    public EmptyTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] state = super.onCreateDrawableState(extraSpace + 1);
        if (mEmptyText) {
            mergeDrawableStates(state, EMPTY_TEXT_STATE);
        } else if (mErrorActive) {
            mergeDrawableStates(state, ERROR_TEXT_STATE);
        }
        return state;
    }

    @Override
    public void setEnabled(boolean enabled) {
        // Apparently the androidX version of this view breaks the hint, so that it is hidden if the
        // view is disabled. Therefore I'm never disabling the view, merely passing on the event to
        // the child
        // This also breaks isEnabled(), I couldn't find a better solution yet
        if (mEditText != null) mEditText.setEnabled(enabled);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof EditText) {
            addEditText((EditText) child);
        }
        super.addView(child, index, params);
    }

    protected void addEditText(EditText child) {
        mEditText = child;
        if (!TextUtils.isEmpty(mEditText.getText())) {
            setEmptyTextState(false);
        }
        mEditText.addTextChangedListener(new TextWatcher() {
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
                if (TextUtils.isEmpty(editable)) {
                    setEmptyTextState(true);
                } else {
//                    setEmptyTextState(false);
                }
            }
        });

        setTypeface(mEditText.getTypeface());
    }

    @Override
    public void setErrorEnabled(boolean enabled) {
        mErrorActive = enabled;
        super.setErrorEnabled(enabled);

        // Workaround to tint the bottom bar but do not show an error message
        if (enabled && mIndicator == null) {
            try {
                // Hint Color
                Field field = this.getClass().getSuperclass().getDeclaredField("indicatorViewController");
                field.setAccessible(true);
                Object indicatorViewController = field.get(this);

                field = indicatorViewController.getClass().getDeclaredField("indicatorArea");
                field.setAccessible(true);
                mIndicator = (LinearLayout) field.get(indicatorViewController);

            } catch (NoSuchFieldException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (IllegalAccessException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }

        if (enabled && mIndicator != null) {
            mIndicator.setVisibility(GONE);
        }
    }

    /**
     * Sets empty state programmatically.
     *
     * @param emptyTextState Empty state
     */
    public void setEmptyTextState(boolean emptyTextState) {
        this.mEmptyText = emptyTextState;
        refreshDrawableState();
    }


    /**
     * Returns the text inside the EditText view.
     *
     * @return Text inside EditText.
     */
    @NonNull
    public Editable getText() {
        return getEditText() != null ? getEditText().getText() :
                new Editable.Factory().newEditable("");
    }

    @Override
    public void setEmptyError(boolean error) {
        setError(error ? " " : null);
        setErrorEnabled(error);
    }

    @Override
    public void requestDelayedFocus() {
        requestFocus();
    }
}
