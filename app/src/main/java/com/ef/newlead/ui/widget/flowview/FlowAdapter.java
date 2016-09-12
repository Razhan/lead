package com.ef.newlead.ui.widget.flowview;

import android.content.Context;

import com.ef.newlead.ui.widget.recycleview.BasicRecyclerViewAdapter;
import com.ef.newlead.ui.widget.recycleview.MultipleTypeItem;
import com.ef.newlead.ui.widget.recycleview.MultipleTypeRecyclerViewAdapter;

import java.util.List;

public abstract class FlowAdapter<T> extends BasicRecyclerViewAdapter<T> {

    protected int border;

    public FlowAdapter(Context context, List<T> list) {
        super(context, list);
    }

    public abstract void setBorderPosition(int pos);

    public int getBorder() {
        return border;
    }
}