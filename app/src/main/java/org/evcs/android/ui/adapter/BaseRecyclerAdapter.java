package org.evcs.android.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseRecyclerAdapter<T, V extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<V> {

    private final List<T> mDataset;
    private final List<T> mUnfilteredDataset;

    private BaseRecyclerAdapterItemClickListener mItemClickListener;

    public BaseRecyclerAdapter() {
        mDataset = new ArrayList<>();
        mUnfilteredDataset = new ArrayList<>();
    }

    public void setItemClickListener(BaseRecyclerAdapterItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull final V holder) {
        super.onViewAttachedToWindow(holder);

        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItemClickListener != null) {
                            int adapterPosition = holder.getAdapterPosition();
                            if (adapterPosition >= 0) {
                                mItemClickListener.onItemClicked(get(adapterPosition), adapterPosition);
                            }
                        }
                    }
                });
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull V holder) {
        holder.itemView.setOnClickListener(null);

        super.onViewDetachedFromWindow(holder);
    }

    public T get(int position) {
        return mDataset.get(position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public final void insert(T elem, int position) {
        mDataset.add(position, elem);
        mUnfilteredDataset.add(position, elem);
        notifyItemInserted(modifyItemPosition(position));
    }

    public final void appendTop(T elem) {
        insert(elem, 0);
    }

    public final void appendTopAll(Collection<T> elems) {
        mDataset.addAll(0, elems);
        mUnfilteredDataset.addAll(0, elems);
        notifyItemRangeInserted(0, elems.size());
    }

    public final void appendBottom(T elem) {
        insert(elem, mDataset.size());
    }

    public void appendBottomAll(Collection<T> elems) {
        int startIndex = mDataset.size();
        mDataset.addAll(startIndex, elems);
        mUnfilteredDataset.addAll(startIndex, elems);
        notifyItemRangeInserted(modifyItemPosition(startIndex), elems.size());
    }

    public void remove(int position) {
        mDataset.remove(position);
        mUnfilteredDataset.remove(position);
        notifyItemRemoved(position);
    }

    public final void remove(T elem) {
        int index = mDataset.indexOf(elem);
        if (index != -1) {
            remove(index);
        }
    }

    public final void update(T elem) {
        int pos = mDataset.indexOf(elem);
        mDataset.set(pos, elem);
        mUnfilteredDataset.set(pos, elem);
        pos = modifyItemPosition(pos);
        notifyItemChanged(pos);
    }

    public final void update(int position) {
        notifyItemChanged(position);
    }

    public final void clear() {
        int size = mDataset.size();
        mDataset.clear();
        mUnfilteredDataset.clear();
        notifyItemRangeRemoved(modifyItemPosition(0), modifyItemPosition(size));
    }

    public final void filter(Predicate<T> predicate) {
        int size = mDataset.size();
        mDataset.clear();
        notifyItemRangeRemoved(0, size);
        int pos = 0;
        for (T elem : mUnfilteredDataset) {
            if (predicate.apply(elem)) {
                mDataset.add(elem);
                notifyItemInserted(pos++);
            }
        }
    }

    public interface Predicate<T> {
        boolean apply(T elem);
    }

    public final void unfilter() {
        int size = mDataset.size();
        mDataset.clear();
        notifyItemRangeRemoved(0, size);
        mDataset.addAll(mUnfilteredDataset);
        notifyItemRangeInserted(0, mDataset.size());
    }

    public final int getItemPosition(T elem) {
        return mDataset.indexOf(elem);
    }

    protected View inflateView(ViewGroup parent, int layout) {
        return LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull final V holder, int position) {
        populate(holder, get(position), position);
    }

    //To be overridden by subclasses when the position of the item in the Dataset is different from
    //the one on the list (Usually when there is a header or a footer)
    protected int modifyItemPosition(int pos) {
        return pos;
    }

    protected abstract void populate(V holder, T item, int position);

}
