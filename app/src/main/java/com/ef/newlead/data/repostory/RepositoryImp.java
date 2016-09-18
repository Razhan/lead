package com.ef.newlead.data.repostory;

import com.ef.newlead.data.model.DataBean.BaseResponse;
import com.ef.newlead.data.model.DataBean.LessonBean;
import com.ef.newlead.data.model.DataBean.LessonPackBean;
import com.ef.newlead.data.model.DataBean.ResourceBean;
import com.ef.newlead.data.model.DataBean.Response;
import com.ef.newlead.data.model.DataBean.UserBean;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import rx.Observable;

public class RepositoryImp implements Repository {

    private static RepositoryImp instance;
    private DataSourceFactory sourceFactory;

    private RepositoryImp() {
        this.sourceFactory = new DataSourceFactory();
    }

    public static Repository getInstance() {
        if (instance == null) {
            instance = new RepositoryImp();
        }

        return instance;
    }

    @Override
    public Observable<ResponseBody> downloadFile(String url) {
        return sourceFactory.getRestfulSource().downloadFile(url);
    }

    @Override
    public Observable<Response<ResourceBean>> resourceInfo() {
        return sourceFactory.getRestfulSource().resourceInfo();
    }

    @Override
    public Observable<Response<UserBean>> getUserInfo(Map<String, String> startInfo) {
        return sourceFactory.getRestfulSource().getUserInfo(startInfo);
    }

    @Override
    public Observable<BaseResponse> getVerificationCode(String number) {
        return sourceFactory.getRestfulSource().getVerificationCode(number);
    }

    @Override
    public Observable<BaseResponse> verifyCode(String number, String code) {
        return sourceFactory.getRestfulSource().verifyCode(number, code);
    }

    @Override
    public Observable<BaseResponse> submitUserInfo(String token, Map<String, String> userInfo) {
        return sourceFactory.getRestfulSource().submitUserInfo(token, userInfo);
    }

    @Override
    public Observable<Response<List<LessonBean>>> getLessonList() {
        return sourceFactory.getRestfulSource().getLessonList();
    }

    @Override
    public Observable<Response<LessonPackBean>> getLessonPack(String id) {
        return sourceFactory.getRestfulSource().getLessonPack(id);
    }
}
