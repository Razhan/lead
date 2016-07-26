package com.ef.cat.data.model;

import com.ran.delta.widget.recycleView.MultipleTypeItem;

import java.util.ArrayList;
import java.util.List;

public class Level implements MultipleTypeItem {

    private String value;
    public boolean isBorder = false;

    public Level(String level) {
        this.value = level;
    }

    public Level(boolean border) {
        this.isBorder = border;
    }

    @Override
    public int getType() {
        return MultipleTypeItem.TYPE_ITEM;
    }

    public String getValue() {
        return value;
    }

    public boolean isBorder() {
        return isBorder;
    }

    public static List<Level> generateLevels(List<String> values) {
        List<Level> res = new ArrayList<>();

        for (String value : values) {
            res.add(new Level(value));
        }

        return res;
    }
}

