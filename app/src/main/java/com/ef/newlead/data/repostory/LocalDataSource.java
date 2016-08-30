package com.ef.newlead.data.repostory;

import com.ef.newlead.data.model.DataBean.BaseResponse;
import com.ef.newlead.data.model.DataBean.ResourceBean;
import com.ef.newlead.data.model.DataBean.UserBean;
import com.ef.newlead.data.model.DataBean.Response;

import java.util.Map;

import okhttp3.ResponseBody;
import rx.Observable;

public class LocalDataSource implements Repository {

    @Override
    public Observable<ResponseBody> downloadFile(String url) {
        return Observable.empty();
    }

    @Override
    public Observable<Response<ResourceBean>> resourceInfo() {
        return Observable.empty();
    }

    @Override
    public Observable<Response<UserBean>> getUserInfo(Map<String, String> startInfo) {
        return Observable.empty();
    }

    @Override
    public Observable<BaseResponse> verifyCode(String number, String code) {
        return Observable.empty();
    }

    @Override
    public Observable<BaseResponse> submitUserInfo(String token, Map<String, String> userInfo) {
        return Observable.empty();
    }
}
