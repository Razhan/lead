package com.ran.delta.widget.recycleView;

public interface MultipleTypeItem {
    int TYPE_HEADER = -2;
    int TYPE_ITEM = -1;
    int TYPE_FOOTER = -3;

    int getType();
}
