package com.ef.newlead.ui.widget.recycleview;

import android.support.v7.widget.GridLayoutManager;

public class HeaderSpanSizeLookup<T extends MultipleTypeItem> extends GridLayoutManager.SpanSizeLookup {

    private final MultipleTypeRecyclerViewAdapter<T> adapter;
    private final GridLayoutManager layoutManager;

    public HeaderSpanSizeLookup(MultipleTypeRecyclerViewAdapter<T> adapter, GridLayoutManager layoutManager) {
        this.adapter = adapter;
        this.layoutManager = layoutManager;
    }

    @Override
    public int getSpanSize(int position) {
        boolean isHeaderOrFooter = adapter.getItem(position).getType() != MultipleTypeItem.ITEM;
        return isHeaderOrFooter ? layoutManager.getSpanCount() : 1;
    }
}