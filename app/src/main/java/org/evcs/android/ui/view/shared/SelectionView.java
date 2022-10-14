package org.evcs.android.ui.view.shared;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.evcs.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectionView extends LinearLayout {

    @BindView(R.id.view_selection_title) TextView mTitle;
    @BindView(R.id.view_selection_description) TextView mDescription;

    public SelectionView(Context context) {
        super(context);
    }

    public SelectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        populateView(context, attrs);
    }

    public SelectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        populateView(context, attrs);
    }

    private void populateView(Context context, AttributeSet attrs) {
        View view = inflate(context, R.layout.view_selection, this);
        ButterKnife.bind(this, view);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectionView);
        String titleResource = typedArray.getString(R.styleable.SelectionView_title);
        String descriptionResource = typedArray.getString(R.styleable.SelectionView_description);
        Drawable imageResource = typedArray.getDrawable(R.styleable.SelectionView_image);
        typedArray.recycle();

//        mTitle.setText(titleResource);
//        mDescription.setText(descriptionResource);
        setBackground(imageResource);
    }

    public void setDescription(String description) {
//        mDescription.setText(description);
    }

    public void setTitle(String title) {
//        mTitle.setText(title);
    }
}
