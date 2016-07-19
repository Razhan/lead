package com.ran.delta.widget.recycleView;

import android.view.View;

public interface OnItemLongClickListener<T> {
    void onItemLongClick(View itemView, int pos, T item);
}
