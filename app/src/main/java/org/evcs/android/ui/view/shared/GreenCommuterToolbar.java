package org.evcs.android.ui.view.shared;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import org.evcs.android.R;
import org.evcs.android.ui.drawer.IToolbarView;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.ButterKnife;

public class GreenCommuterToolbar extends Toolbar {

    @BindColor(R.color.evcs_black) int mBlack;
    @BindColor(R.color.evcs_transparent_black) int mBlackTransparent;
    @BindColor(R.color.evcs_gray_89) int mEvenDarkerGray;

    private CharSequence mSavedTitle;
    private ArrayList<Boolean> mIconsState = new ArrayList<>();
    private boolean mIsShowing;

    public GreenCommuterToolbar(Context context) {
        super(context);
        init(context);
    }

    public GreenCommuterToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GreenCommuterToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        ButterKnife.bind(this);
        setBackgroundColor(ContextCompat.getColor(context, R.color.evcs_black));
        setOverflowIcon(ContextCompat.getDrawable(context, R.drawable.ic_more));
    }

    public void setToolbarState(IToolbarView.ToolbarState toolbarState) {
        switch (toolbarState) {
            case SHOW_ARROW:
            case SHOW_HAMBURGER:
                setBackgroundColor(mEvenDarkerGray);
//                setBackgroundColor(mBlack);
                setTitleTextAppearance(getContext(), R.style.ToolbarTitle);
                break;
            case TRANSPARENT_TOOLBAR:
                setBackgroundColor(mBlackTransparent);
                setTitleTextAppearance(getContext(), R.style.ToolbarTitle_New);
                break;
            case SHOW_HAMBURGUER_NEW:
            case SHOW_ARROW_NEW:
                setBackgroundColor(mEvenDarkerGray);
                setTitleTextAppearance(getContext(), R.style.ToolbarTitle_New);
            default:
                break;
        }
        setVisibility(toolbarState == IToolbarView.ToolbarState.HIDE_TOOLBAR ? GONE : VISIBLE);
    }

    @Override
    public void setTitle(int resId) {
        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mSavedTitle = title;
    }

    public void showTitleAndIcons(boolean show) {
        if (mIsShowing == show) return;
        mIsShowing = show;
        super.setTitle(show ? mSavedTitle : null);
        if (!show) {
            mIconsState = new ArrayList<>();
            for (int i = 0; i < getMenu().size(); i++) {
                mIconsState.add(getMenu().getItem(i).isVisible());
            }
        }
        for (int i = 0; i < getMenu().size(); i++) {
            getMenu().getItem(i).setVisible(show && (i >= mIconsState.size() || mIconsState.get(i)));
        }

    }
}
