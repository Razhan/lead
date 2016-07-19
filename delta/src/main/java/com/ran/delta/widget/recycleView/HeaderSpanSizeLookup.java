package com.ran.delta.widget.recycleView;

import android.support.v7.widget.GridLayoutManager;

public class HeaderSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private final HeaderRecyclerViewAdapter adapter;
    private final GridLayoutManager layoutManager;

    public HeaderSpanSizeLookup(HeaderRecyclerViewAdapter adapter, GridLayoutManager layoutManager) {
        this.adapter = adapter;
        this.layoutManager = layoutManager;
    }

    @Override
    public int getSpanSize(int position) {
        boolean isHeaderOrFooter =
                adapter.isHeaderPosition(position) || adapter.isFooterPosition(position);
        return isHeaderOrFooter ? layoutManager.getSpanCount() : 1;
    }
}