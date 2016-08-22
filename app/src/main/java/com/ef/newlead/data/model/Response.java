package com.ef.newlead.data.model;

public class Response<T> extends BaseResponse {

    private T data;

    public T getData() {
        return data;
    }
}
