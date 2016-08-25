package com.ef.newlead.data.model;

import com.google.gson.annotations.SerializedName;

public class Level {

    private String id;

    @SerializedName("level_title")
    private String levelTitle;

    @SerializedName("level_detail")
    private String levelDetail;

    @SerializedName("level_example")
    private String levelExample;

    private int order;

    private boolean isBorder = false;

    public Level(boolean border) {
        this.isBorder = border;
    }

    public boolean isBorder() {
        return isBorder;
    }

    public String getId() {
        return id;
    }

    public String getLevelTitle() {
        return levelTitle;
    }

    public String getLevelDetail() {
        return levelDetail;
    }

    public String getLevelExample() {
        return levelExample;
    }

    public int getOrder() {
        return order;
    }


}

