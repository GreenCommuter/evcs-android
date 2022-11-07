package org.evcs.android.ui.adapter;

/**
 * Listener class to expose item attributes when an item view bound to the
 * {@link BaseRecyclerAdapter} is clicked.
 */
public abstract class BaseRecyclerAdapterItemClickListener<T> {

    public abstract void onItemClicked(T item, int adapterPosition);

}