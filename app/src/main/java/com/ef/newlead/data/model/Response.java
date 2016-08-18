package com.ef.newlead.data.model;

import com.google.gson.annotations.SerializedName;

public class Response<T> extends BaseResponse {

    private T data;

    public T getData() {
        return data;
    }
}
