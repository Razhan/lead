package com.ef.newlead.data.model;

import com.ef.newlead.ui.widget.recycleview.MultipleTypeItem;

public abstract class ItemData implements MultipleTypeItem {

    @Override
    public int getType() {
        return MultipleTypeItem.ITEM;
    }

}
