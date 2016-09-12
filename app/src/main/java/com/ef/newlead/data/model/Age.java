package com.ef.newlead.data.model;

import com.google.gson.annotations.SerializedName;

public class Age {

    public boolean isBorder = false;

    @SerializedName("age_select_age")
    private String age;

    @SerializedName("age_select_id")
    private String id;

    @SerializedName("age_select_order")
    private int order;

    public Age(boolean border) {
        this.isBorder = border;
    }

    public boolean isBorder() {
        return isBorder;
    }

    public void setBorder(boolean border) {
        isBorder = border;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
