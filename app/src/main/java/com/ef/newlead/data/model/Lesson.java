package com.ef.newlead.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by seanzhou on 9/9/16.
 */
public class Lesson {
    private String id;

    private String title;

    @SerializedName("title-zh-cn")
    private String titleCN;

    private List<ActivityTemplate> activities;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleCN() {
        return titleCN;
    }

    public List<ActivityTemplate> getActivities() {
        return activities;
    }
}
