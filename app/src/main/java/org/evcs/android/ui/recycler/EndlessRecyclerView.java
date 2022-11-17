package org.evcs.android.ui.recycler;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Adds support for endless scrolling to {@link RecyclerView}. A {@link LoadMoreListener} needs
 * to be provided in order to be notified for when the view is rendering its last items. The amount
 * of items left before notifying can be customized by using
 * {@link EndlessRecyclerView#setItemsVisibleThreshold(int)} or
 * {@link EndlessRecyclerView#setUp(boolean, LoadMoreListener, int)}.
 */
public class EndlessRecyclerView extends RecyclerView {

    private boolean mEndlessScrollingEnabled;
    private boolean mIsLoading;
    private int mItemsVisibleThreshold;

    private LoadMoreListener mLoadMoreListener;
    private EndlessScrollListener mEndlessScrollListener;

    public EndlessRecyclerView(Context context) {
        super(context);
        init();
    }

    public EndlessRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EndlessRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mIsLoading = false;
        mEndlessScrollListener = new EndlessScrollListener();
        addOnScrollListener(mEndlessScrollListener);
        // Forces user to set it to 'true' in order to use endless scrolling
        toggleEndlessScrolling(false);
    }

    /**
     * Shorthands several setters.
     *
     * @param endlessScrollingEnabled endlessScrolling state
     * @param listener listener
     * @param itemsVisibleThreshold itemsVisible threshold
     */
    public void setUp(boolean endlessScrollingEnabled, @Nullable LoadMoreListener listener, int itemsVisibleThreshold) {
        setLoadMoreListener(listener);
        setItemsVisibleThreshold(itemsVisibleThreshold);
        toggleEndlessScrolling(endlessScrollingEnabled);
    }

    /**
     * Sets {@link LoadMoreListener} to use.
     *
     * @param listener target listener
     */
    public void setLoadMoreListener(@Nullable LoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    /**
     * Toggles endless scrolling. If enabled, it triggers the {@link LoadMoreListener}, if needed.
     *
     * @param enable
     */
    public void toggleEndlessScrolling(boolean enable) {
        mEndlessScrollingEnabled = enable;

        if (mEndlessScrollingEnabled) {
            post(new Runnable() {
                @Override
                public void run() {
                    if (mEndlessScrollListener.isVisibilityThresholdExceeded()) {
                        mLoadMoreListener.onLoadMore();
                    }
                }
            });
        }
    }

    /**
     * Sets the visibility threshold.
     *
     * @param itemsVisibleThreshold new visibility threshold
     */
    public void setItemsVisibleThreshold(int itemsVisibleThreshold) {
        mItemsVisibleThreshold = itemsVisibleThreshold;
    }

    private void triggerLoadMoreListenerIfSet() {
        if (mLoadMoreListener != null) {
            mIsLoading = true;
            mLoadMoreListener.onLoadMore();
        }
    }

    /**
     * Executes internal logic to allow the listener to notify of load more events.
     */
    public void notifyLoadingFinished() {
        mIsLoading = false;
    }

    public interface LoadMoreListener {
        /**
         * @return boolean that, if raised, signals it shouldn't trigger the listener anymore
         *                       else it continues to trigger it
         */
        void onLoadMore();
    }

    /**
     * Listens to {@link RecyclerView}'s scroll events and checks, if necessary, that the visibility
     * threshold is exceeded. In case it does, notifies the {@link LoadMoreListener} that it should
     * load more elements.
     */
    private class EndlessScrollListener extends OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (mIsLoading || !mEndlessScrollingEnabled) {
                return;
            }
            if (isVisibilityThresholdExceeded()) {
                triggerLoadMoreListenerIfSet();
            }
        }

        /**
         * Checks if the recycler is scrolled enough to break the item visibility threshold.
         *
         * Note that it assumes that the recycler view items are shown in adapter indexing order.
         * This means that for an item with index i, views with index j > i are shown
         * either at the same time or after the i view has been show. (i.e.: how a
         * {@link LinearLayoutManager} displays them).
         *
         * Knowing this, we can check the visibility of only the last bound view to check if the
         * threshold is exceeded.
         *
         * @return true if child view at index '(layoutManager.getChildCount() - 1)' is at least
         * at index '(layoutManager.itemCount - 1) - itemsVisibleThreshold' AND is partially visible,
         * else false
         */
        private boolean isVisibilityThresholdExceeded() {
            LayoutManager manager = getLayoutManager();
            int itemCount = manager.getItemCount();

            // Early return if the total items amount is less or equal to the threshold
            if (itemCount <= mItemsVisibleThreshold) {
                return true;
            }

            // Get the last bound child
            View child = manager.getChildAt(manager.getChildCount() - 1);

            // Position of the view in the adapter
            int childAdapterIndex = getChildAdapterPosition(child);

            // Early return false if something goes wrong
            if (childAdapterIndex == NO_POSITION) {
                return false;
            }

            int visibilityThresholdIndex = (itemCount - 1) - mItemsVisibleThreshold;

            // It's a view that its index doesn't exceed the threshold
            if (childAdapterIndex < visibilityThresholdIndex) {
                return false;
            }

            // If the view is partially visible, return true, else it means that the view is bound
            // but still not shown so we return false
            OrientationHelper helper = manager.canScrollVertically()
                    ? OrientationHelper.createVerticalHelper(manager)
                    : OrientationHelper.createHorizontalHelper(manager);

            int end = helper.getEndAfterPadding();
            int childStart = helper.getDecoratedStart(child);

            return childStart < end;
        }
    }

}
