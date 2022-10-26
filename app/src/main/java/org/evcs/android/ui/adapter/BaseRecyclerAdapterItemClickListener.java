package org.evcs.android.ui.adapter;

/**
 * Listener class to expose item attributes when an item view bound to the
 * {@link org.greencommuter.android.ui.adapter.BaseRecyclerAdapter} is clicked.
 */
public abstract class BaseRecyclerAdapterItemClickListener<T> {

    public abstract void onItemClicked(T item, int adapterPosition);

}