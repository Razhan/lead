package com.ef.newlead.data.repostory;

import com.ef.newlead.data.model.DataBean.ResourceBean;
import com.ef.newlead.data.model.DataBean.UserBean;
import com.ef.newlead.data.model.Response;

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
    public Observable<Response<UserBean>> getUserInfo(String device, String campaign,
                                                      String source, String appStore) {
        return Observable.empty();
    }
}
