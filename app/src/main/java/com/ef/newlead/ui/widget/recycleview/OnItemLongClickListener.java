package com.ef.newlead.ui.widget.recycleview;

import android.view.View;

public interface OnItemLongClickListener<T> {
    void onItemLongClick(View itemView, int pos, T item);
}
