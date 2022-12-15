package org.evcs.android.ui.view.shared;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import org.evcs.android.R;

import java.util.ArrayList;

public class EVCSToolbar extends Toolbar {

//    @BindColor(R.color.evcs_gray_89) int mEvenDarkerGray;

//    private CharSequence mSavedTitle;
//    private ArrayList<Boolean> mIconsState = new ArrayList<>();
//    private boolean mIsShowing;

    public EVCSToolbar(Context context) {
        super(context);
        init(context);
    }

    public EVCSToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EVCSToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setBackgroundColor(Color.parseColor("#2D1142"));
        setOverflowIcon(ContextCompat.getDrawable(context, R.drawable.ic_more));
        setTitleTextColor(getResources().getColor(R.color.evcs_white));
        setPadding(0, (int) getResources().getDimension(R.dimen.status_bar_height), 0, 0);
    }

//    public void setToolbarState(IToolbarView.ToolbarState toolbarState) {
//        switch (toolbarState) {
//            case SHOW_ARROW:
//            case SHOW_HAMBURGER:
//                setBackgroundColor(mEvenDarkerGray);
//                setTitleTextAppearance(getContext(), R.style.ToolbarTitle);
//                break;
//            default:
//                break;
//        }
//        setVisibility(toolbarState == IToolbarView.ToolbarState.HIDE_TOOLBAR ? GONE : VISIBLE);
//    }

    @Override
    public void setTitle(int resId) {
        setTitle(getContext().getText(resId));
    }

//    @Override
//    public void setTitle(CharSequence title) {
//        super.setTitle(title);
//        mSavedTitle = title;
//    }

}
