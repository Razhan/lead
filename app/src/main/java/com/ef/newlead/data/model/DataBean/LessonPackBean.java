package com.ef.newlead.data.model.DataBean;

import com.google.gson.annotations.SerializedName;

public class LessonPackBean {

    @SerializedName("lesson_id")
    private String id;

    @SerializedName("mediapackage_src")
    private String mediaSrc;

    @SerializedName("mediapackage_hash")
    private String mediaHash;

    @SerializedName("jsonpackage_src")
    private String jsonSrc;

    @SerializedName("jsonpackage_hash")
    private String jsonHash;

    public String getId() {
        return id;
    }

    public String getMediaSrc() {
        return mediaSrc;
    }

    public String getMediaHash() {
        return mediaHash;
    }

    public String getJsonSrc() {
        return jsonSrc;
    }

    public String getJsonHash() {
        return jsonHash;
    }
}
