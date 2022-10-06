package org.evcs.android.ui.view.profile;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.evcs.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListButtonView extends LinearLayout {

    @BindView(R.id.view_list_button_text) TextView mText;
    @BindView(R.id.view_list_button_bottom_line) View mBottonLine;

    public ListButtonView(Context context) {
        super(context);
    }

    public ListButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ListButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = inflate(context, R.layout.view_list_button, this);
        ButterKnife.bind(this, view);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ListButtonView);
        String text = typedArray.getString(R.styleable.ListButtonView_text);
        boolean bottomDiv = typedArray.getBoolean(R.styleable.ListButtonView_bottomDivider, true);
        typedArray.recycle();

        mText.setText(text);
        mBottonLine.setVisibility(bottomDiv ? VISIBLE : INVISIBLE);
        setClickable(true);
    }

    public void setText(String text) {
        mText.setText(text);
    }

    public void setText(@StringRes int resId) {
        mText.setText(resId);
    }

}
