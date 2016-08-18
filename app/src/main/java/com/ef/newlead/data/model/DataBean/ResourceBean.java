package com.ef.newlead.data.model.DataBean;

import com.google.gson.annotations.SerializedName;

public class ResourceBean {

    @SerializedName("res_id")
    private String resId;
    private String src;
    private String hash;

    public String getResId() {
        return resId;
    }

    public String getSrc() {
        return src;
    }

    public String getHash() {
        return hash;
    }
}
