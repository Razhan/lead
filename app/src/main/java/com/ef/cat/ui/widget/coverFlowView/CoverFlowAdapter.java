package com.ef.cat.ui.widget.coverFlowView;

import android.content.Context;

import com.ran.delta.widget.recycleView.MultipleTypeItem;
import com.ran.delta.widget.recycleView.MultipleTypeRecyclerViewAdapter;

import java.util.List;

public abstract class CoverFlowAdapter<T extends MultipleTypeItem> extends MultipleTypeRecyclerViewAdapter<T> {

    public CoverFlowAdapter(Context context, List<T> list) {
        super(context, list);
    }

    public abstract void setBorderPosition(int pos);

}