package com.ef.newlead.domain.usecase;

import com.ef.newlead.data.model.DataBean.ResourceBean;
import com.ef.newlead.data.model.DataBean.Response;
import com.ef.newlead.data.model.DataBean.UserBean;

import java.util.Map;

import okhttp3.ResponseBody;
import rx.Observable;

public class InitializationUseCase extends UseCase {

    @UseCaseMethod(name = "Download")
    public Observable<ResponseBody> getResourceFile(String url) {
        return repository.downloadFile(url);
    }

    @UseCaseMethod(name = "ResourceInfo")
    public Observable<Response<ResourceBean>> getResourceInfo() {
        return repository.resourceInfo();
    }

    @UseCaseMethod
    public Observable<Response<UserBean>> getUserInfo(Map<String, String> startInfo) {
        return repository.getUserInfo(startInfo);
    }

}
