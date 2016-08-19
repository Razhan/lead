package com.ef.newlead.data.repostory;

import com.ef.newlead.data.model.DataBean.ResourceBean;
import com.ef.newlead.data.model.DataBean.UserBean;
import com.ef.newlead.data.model.Response;

import okhttp3.ResponseBody;
import rx.Observable;

public interface Repository {

    Observable<ResponseBody> downloadFile(String url);

    Observable<Response<ResourceBean>> resourceInfo();

    Observable<Response<UserBean>> getUserInfo(String device, String campaign, String source, String appStore);

}
