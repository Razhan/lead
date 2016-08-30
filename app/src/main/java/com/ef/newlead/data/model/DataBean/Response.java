package com.ef.newlead.data.model.DataBean;

public class Response<T> extends BaseResponse {

    private T data;

    public T getData() {
        return data;
    }
}
