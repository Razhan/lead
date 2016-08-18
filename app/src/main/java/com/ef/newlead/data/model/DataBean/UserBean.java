package com.ef.newlead.data.model.DataBean;

import com.google.gson.annotations.SerializedName;

public class UserBean {
    @SerializedName("user_id")
    private String id;

    private String token;

    @SerializedName("abtest_group")
    private String group;

    @SerializedName("abtest_rule")
    private String rule;

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getGroup() {
        return group;
    }

    public String getRule() {
        return rule;
    }
}


