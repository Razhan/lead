package com.ef.newlead.ui.widget.flowview;

import android.content.Context;

import com.ef.newlead.ui.widget.recycleview.MultipleTypeItem;
import com.ef.newlead.ui.widget.recycleview.MultipleTypeRecyclerViewAdapter;

import java.util.List;

public abstract class FlowAdapter<T extends MultipleTypeItem> extends MultipleTypeRecyclerViewAdapter<T> {

    public FlowAdapter(Context context, List<T> list) {
        super(context, list);
    }

    public abstract void setBorderPosition(int pos);

}