package com.ef.newlead.data.model;

import com.ef.newlead.ui.widget.recycleview.MultipleTypeItem;

import java.util.ArrayList;
import java.util.List;

public class Interest implements MultipleTypeItem {

    private final String value;

    public Interest(String interest) {
        this.value = interest;
    }

    public static List<Interest> generateInterests(List<String> values) {
        List<Interest> res = new ArrayList<>();

        for (String value : values) {
            res.add(new Interest(value));
        }

        return res;
    }

    @Override
    public int getType() {
        return MultipleTypeItem.ITEM;
    }

    public String getValue() {
        return value;
    }
}
