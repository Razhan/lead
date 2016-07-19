package com.ran.delta.widget.recycleView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

public abstract class HeaderRecyclerViewAdapter<VH extends ViewHolder, H, T, F>
        extends RecyclerView.Adapter<VH> {

    protected static final int TYPE_HEADER = -2;
    protected static final int TYPE_ITEM = -1;
    protected static final int TYPE_FOOTER = -3;

    private H header;
    private List<T> items = Collections.EMPTY_LIST;
    private F footer;
    private boolean showFooter = true;

    private OnItemClickListener<T> mClickListener;
    private OnItemLongClickListener<T> mLongClickListener;

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH viewHolder;
        if (isHeaderType(viewType)) {
            viewHolder = onCreateHeaderViewHolder(parent, viewType);
        } else if (isFooterType(viewType)) {
            viewHolder = onCreateFooterViewHolder(parent, viewType);
        } else {
            viewHolder = onCreateItemViewHolder(parent, viewType);
        }

        if (mClickListener != null) {
            viewHolder.itemView.setOnClickListener(v ->
                    mClickListener.onItemClick(v, viewHolder.getLayoutPosition(),
                            items.get(viewHolder.getLayoutPosition()))
            );
        }
        if (mLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(v -> {
                mLongClickListener.onItemLongClick(v, viewHolder.getLayoutPosition(),
                        items.get(viewHolder.getLayoutPosition()));
                return true;
            });
        }

        return viewHolder;
    }

    protected VH onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    protected abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    protected VH onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    public void setClickListener(OnItemClickListener<T> listener) {
        mClickListener = listener;
    }

    public void setLongClickListener(OnItemLongClickListener<T> listener) {
        mLongClickListener = listener;
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        if (isHeaderPosition(position)) {
            onBindHeaderViewHolder(holder, position);
        } else if (isFooterPosition(position)) {
            onBindFooterViewHolder(holder, position);
        } else {
            onBindItemViewHolder(holder, position);
        }
    }

    protected void onBindHeaderViewHolder(VH holder, int position) {
    }

    protected abstract void onBindItemViewHolder(VH holder, int position);

    protected void onBindFooterViewHolder(VH holder, int position) {
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = TYPE_ITEM;
        if (isHeaderPosition(position)) {
            viewType = TYPE_HEADER;
        } else if (isFooterPosition(position)) {
            viewType = TYPE_FOOTER;
        }
        return viewType;
    }

    @Override
    public int getItemCount() {
        int size = items.size();
        if (hasHeader()) {
            size++;
        }
        if (hasFooter()) {
            size++;
        }
        return size;
    }

    public H getHeader() {
        return header;
    }

    public void setHeader(H header) {
        this.header = header;
    }

    public T getItem(int position) {
        if (hasHeader() && hasItems()) {
            --position;
        }
        return items.get(position);
    }

    public F getFooter() {
        return footer;
    }

    public void setFooter(F footer) {
        this.footer = footer;
    }

    public void set(@NonNull List<T> items) {
        items.clear();
        validateItems(items);
        items.addAll(items);
        notifyDataSetChanged();
    }

    public void add(int pos, T item) {
        items.add(pos, item);
        notifyItemInserted(pos);
    }

    public void add(@NonNull List<T> more) {
        if (!more.isEmpty()) {
            int currentSize = items.size();
            int amountInserted = more.size();

            items.addAll(more);
            notifyItemRangeInserted(currentSize, amountInserted);
        }
    }

    public List<T> getItems() {
        return items;
    }

    public void delete(int pos) {
        if (hasHeader() && hasItems()) {
            --pos;
        }

        T item = items.get(pos);

        if (items.contains(item)) {
            items.remove(pos);
        }

        notifyItemRemoved(pos);
    }

    public void clear() {
        items.clear();
    }

    public void showFooter() {
        this.showFooter = true;
        notifyDataSetChanged();
    }

    public void hideFooter() {
        this.showFooter = false;
        notifyDataSetChanged();
    }

    public boolean isHeaderPosition(int position) {
        return hasHeader() && position == 0;
    }

    public boolean isFooterPosition(int position) {
        int lastPosition = getItemCount() - 1;
        return hasFooter() && position == lastPosition;
    }

    protected boolean isHeaderType(int viewType) {
        return viewType == TYPE_HEADER;
    }

    protected boolean isFooterType(int viewType) {
        return viewType == TYPE_FOOTER;
    }

    protected boolean hasHeader() {
        return getHeader() != null;
    }

    protected boolean hasFooter() {
        return getFooter() != null && showFooter;
    }

    private boolean hasItems() {
        return items.size() > 0;
    }

    private void validateItems(List<T> items) {
        if (items == null) {
            throw new IllegalArgumentException("You can't use a null List<Item> instance.");
        }
    }
}