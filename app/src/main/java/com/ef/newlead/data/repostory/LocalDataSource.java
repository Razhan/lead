package com.ef.newlead.data.repostory;

import com.ef.newlead.data.model.DataBean.BaseResponse;
import com.ef.newlead.data.model.DataBean.BookInfoBean;
import com.ef.newlead.data.model.DataBean.CenterTimeBean;
import com.ef.newlead.data.model.DataBean.LessonBean;
import com.ef.newlead.data.model.DataBean.LessonPackBean;
import com.ef.newlead.data.model.DataBean.ResourceBean;
import com.ef.newlead.data.model.DataBean.Response;
import com.ef.newlead.data.model.DataBean.UserBean;

import java.util.List;
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
    public Observable<BaseResponse> getVerificationCode(String number) {
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

    @Override
    public Observable<Response<List<LessonBean>>> getLessonList() {
        return Observable.empty();
    }

    @Override
    public Observable<Response<LessonPackBean>> getLessonPack(String id) {
        return Observable.empty();
    }

    @Override
    public Observable<Response<CenterTimeBean>> getCenterTime(String id) {
        return Observable.empty();
    }

    @Override
    public Observable<BaseResponse> bookCenter(Map<String, String> info) {
        return Observable.empty();
    }

    @Override
    public Observable<Response<List<BookInfoBean>>> getBookInfo() {
        return Observable.empty();
    }
}
