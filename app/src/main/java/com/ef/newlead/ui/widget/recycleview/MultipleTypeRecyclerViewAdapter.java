package com.ef.newlead.ui.widget.recycleview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class MultipleTypeRecyclerViewAdapter<T extends MultipleTypeItem> extends BasicRecyclerViewAdapter<T> {

    public MultipleTypeRecyclerViewAdapter(Context context, List<T> list) {
        super(context, list);
    }

    @Override
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

    public abstract int getHeaderLayout();

    public abstract int getFooterLayout();

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

    protected abstract void onBindHeaderViewHolder(ViewHolder holder, int position, T item);

    protected abstract void onBindFooterViewHolder(ViewHolder holder, int position, T item);

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

}