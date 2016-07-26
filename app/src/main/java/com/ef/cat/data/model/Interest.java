package com.ef.cat.data.model;

import com.ran.delta.widget.recycleView.MultipleTypeItem;

import java.util.ArrayList;
import java.util.List;

public class Interest implements MultipleTypeItem {

    private final String value;

    public Interest(String interest) {
        this.value = interest;
    }

    @Override
    public int getType() {
        return MultipleTypeItem.TYPE_ITEM;
    }

    public String getValue() {
        return value;
    }

    public static List<Interest> generateInterests(List<String> values) {
        List<Interest> res = new ArrayList<>();

        for (String value : values) {
            res.add(new Interest(value));
        }

        return res;
    }
}
