package com.ran.delta.widget.recycleView;

import android.view.View;

public interface OnItemClickListener<T> {
    void onItemClick(View itemView, int pos, T item);
}
