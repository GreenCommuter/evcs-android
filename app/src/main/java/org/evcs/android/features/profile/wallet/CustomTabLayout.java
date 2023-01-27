package org.evcs.android.features.profile.wallet;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import org.evcs.android.R;

import java.util.ArrayList;

public class CustomTabLayout extends LinearLayout {

    private Drawable mSelected;
    private Drawable mUnselected;

    private ArrayList<ImageView> mViews = new ArrayList<>();
    private int mMargin;

    public CustomTabLayout(Context context) {
        super(context);
        init(context, null);
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        parseAttrs(attrs);
        setGravity(Gravity.CENTER);
    }

    private void parseAttrs(@Nullable AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray typedArray =
                getContext().obtainStyledAttributes(
                        attrs,
                        R.styleable.CustomTabLayout);

                mSelected = typedArray.getDrawable(R.styleable.CustomTabLayout_selectedTab);
                mUnselected = typedArray.getDrawable(R.styleable.CustomTabLayout_unselectedTab);
                mMargin = Math.round(typedArray.getDimension(R.styleable.CustomTabLayout_innerMargin, 0));

        typedArray.recycle();
    }

    public void setupWithViewPager(@NonNull ViewPager2 viewPager) {
        mViews.clear();
        removeAllViewsInLayout();
        for (int i = 0; i < viewPager.getAdapter().getItemCount(); i++) {
            ImageView v = new ImageView(getContext());
            addView(v);
            LayoutParams lp = new LayoutParams(v.getLayoutParams());
            lp.setMargins(mMargin / 2, 0, mMargin / 2, 0);
            v.setLayoutParams(lp);
            mViews.add(v);
        }
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                setSelected(position);
            }
        });
        if (!mViews.isEmpty())
            setSelected(0);
    }

    public void setSelected(int index) {
        for (ImageView v : mViews) {
            v.setImageDrawable(mUnselected);
        }
        mViews.get(index).setImageDrawable(mSelected);
    }
}
