package com.ef.newlead.data.model;

import com.ef.newlead.ui.widget.recycleview.MultipleTypeItem;

public abstract class OnlyItemData implements MultipleTypeItem {

    @Override
    public int getType() {
        return MultipleTypeItem.ITEM;
    }

}
