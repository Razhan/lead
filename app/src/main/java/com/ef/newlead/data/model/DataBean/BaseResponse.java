package com.ef.newlead.data.model.DataBean;

public class BaseResponse {

    protected int status;
    protected String message;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public boolean isOK() {
        return status == 0;
    }
}
