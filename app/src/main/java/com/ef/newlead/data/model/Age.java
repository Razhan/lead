package com.ef.newlead.data.model;

import com.ef.newlead.ui.widget.recycleview.MultipleTypeItem;

import java.util.ArrayList;
import java.util.List;

public class Age implements MultipleTypeItem {

    public boolean isBorder = false;
    private String value;

    public Age(String interest) {
        this.value = interest;
    }

    public Age(boolean border) {
        this.isBorder = border;
    }

    public static List<Interest> generateAges(List<String> values) {
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

    public boolean isBorder() {
        return isBorder;
    }
}
