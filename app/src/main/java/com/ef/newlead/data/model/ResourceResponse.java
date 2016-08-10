package com.ef.newlead.data.model;

import com.google.gson.annotations.SerializedName;

public class ResourceResponse extends BaseResponse {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
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
}
