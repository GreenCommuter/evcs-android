package org.evcs.android.ui.recycler.decoration;

import static java.lang.annotation.RetentionPolicy.SOURCE;

import android.graphics.Rect;
import androidx.annotation.IntDef;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import java.lang.annotation.Retention;

/**
 * Class extending {@link RecyclerView.ItemDecoration} that adds empty space
 * either to the sides or to the ends of the {@link View}'s attached to the {@link RecyclerView}.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 2;

    private final int mOrientation;

    private final int mSpacing;

    @Retention(SOURCE)
    @IntDef({HORIZONTAL, VERTICAL})
    public @interface Orientation {}

    /**
     * @param spacing amount, in px, of empty space to add
     * @param orientation if {@link SpaceItemDecoration#HORIZONTAL} it adds the space to the side,
     *                    other case it adds it to the top
     *
     * @throws IllegalArgumentException if orientation is not {@link SpaceItemDecoration.Orientation}
     *                                  or if spacing is < 0
     */
    public SpaceItemDecoration(int spacing, @Orientation int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("Orientation should be either SpaceItemDecoration.HORIZONTAL or SpaceItemDecoration.VERTICAL");
        }

        if (spacing < 0) {
            throw new IllegalArgumentException("Spacing should be >= 0");
        }

        mSpacing = spacing;
        mOrientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == HORIZONTAL) {
            outRect.left = mSpacing;
            outRect.right = mSpacing;
        } else {
            outRect.top = mSpacing;
            outRect.bottom = mSpacing;
        }
    }
}
