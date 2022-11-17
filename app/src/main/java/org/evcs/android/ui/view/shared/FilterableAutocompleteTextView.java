package org.evcs.android.ui.view.shared;

import android.content.Context;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;

/**
 * Extension of AppAutoCompleteTextView that allows us to set a text without updating the dropdown
 * filter
 */
public class FilterableAutocompleteTextView extends AppCompatAutoCompleteTextView {

    private boolean enableFilter = true;
    private OnUserChangedListener mListener;
    private boolean fromUser = true;

    public FilterableAutocompleteTextView(Context context) {
        super(context);
    }

    public FilterableAutocompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FilterableAutocompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setUserTextChangedListener(OnUserChangedListener watcher) {
        mListener = watcher;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (fromUser && mListener != null) {
            mListener.onUserChangedText();
        }
    }

    /**
     * Set whether changing the text is going to update the filter
     */
    public void setEnableFilter(boolean enableFilter) {
        this.enableFilter = enableFilter;
    }

    /**
     * Overriden so that if we don't want to filter this returns false
     * @return true if there are enough chars to filter AND filtering is enabled
     */
    @Override
    public boolean enoughToFilter() {
        return enableFilter && super.enoughToFilter();
    }

    /**
     * Turns out this method already exists, but only in API 17.
     * @param text Text to set
     * @param filter If we want to filter or not
     */
    public void setText(String text, boolean filter) {
        if (filter) {
            setText(text);
        } else {
            setEnableFilter(false);
            fromUser = false;
            setText(text);
            fromUser = true;
            setEnableFilter(true);
        }
    }

    public interface OnUserChangedListener {
        void onUserChangedText();
    }
}