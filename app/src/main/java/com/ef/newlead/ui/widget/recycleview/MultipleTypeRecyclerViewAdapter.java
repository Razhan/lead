package com.ef.newlead.ui.widget.recycleview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class MultipleTypeRecyclerViewAdapter<T extends MultipleTypeItem> extends RecyclerView.Adapter<ViewHolder> {

    protected final Context mContext;
    protected LayoutInflater mInflater;
    private List<T> items;
    private OnItemClickListener<T> mClickListener;
    private OnItemLongClickListener<T> mLongClickListener;

    public MultipleTypeRecyclerViewAdapter(Context context, List<T> list) {
        items = (list != null) ? list : new ArrayList();
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public final ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ViewHolder viewHolder = onCreateItemViewHolder(parent, viewType);

        if (mClickListener != null) {
            viewHolder.itemView.setOnClickListener(v ->
                    mClickListener.onItemClick(v, viewHolder.getLayoutPosition(), items.get(viewHolder.getLayoutPosition()))
            );
        }
        if (mLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(v -> {
                mLongClickListener.onItemLongClick(v, viewHolder.getLayoutPosition(), items.get(viewHolder.getLayoutPosition()));
                return true;
            });
        }

        return viewHolder;
    }

    public ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case MultipleTypeItem.HEADER:
                return new ViewHolder(mContext, mInflater.inflate(getHeaderLayout(), parent, false));
            case MultipleTypeItem.ITEM:
                return new ViewHolder(mContext, mInflater.inflate(getItemLayout(), parent, false));
            case MultipleTypeItem.FOOTER:
                return new ViewHolder(mContext, mInflater.inflate(getFooterLayout(), parent, false));
        }
        return null;
    }

    public int getHeaderLayout() {
        return -1;
    }

    public int getFooterLayout() {
        return -1;
    }

    public abstract int getItemLayout();

    public void setClickListener(OnItemClickListener<T> listener) {
        mClickListener = listener;
    }

    public void setLongClickListener(OnItemLongClickListener<T> listener) {
        mLongClickListener = listener;
    }

    @Override
    public final void onBindViewHolder(ViewHolder holder, int position) {
        T item = items.get(position);

        if (item.getType() == MultipleTypeItem.HEADER) {
            onBindHeaderViewHolder(holder, position, item);
        } else if (item.getType() == MultipleTypeItem.FOOTER) {
            onBindFooterViewHolder(holder, position, item);
        } else {
            onBindItemViewHolder(holder, position, item);
        }
    }

    protected void onBindHeaderViewHolder(ViewHolder holder, int position, T item) {
    }

    protected abstract void onBindItemViewHolder(ViewHolder holder, int position, T item);

    protected void onBindFooterViewHolder(ViewHolder holder, int position, T item) {
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public void set(@NonNull List<T> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void add(int pos, T item) {
        items.add(pos, item);
        notifyItemInserted(pos);
    }

    public void add(List<T> more) {
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

    public void remove(int pos) {
        if (items == null || pos < 0 || pos > items.size() - 1) {
            return;
        }

        items.remove(pos);
        notifyItemRemoved(pos);
    }

    public void removeAll() {
        if (items == null) {
            return;
        }

        int count = items.size();
        items.clear();
        notifyItemRangeRemoved(0, count);
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    private boolean hasItems() {
        return items.size() > 0;
    }
}