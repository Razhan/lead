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
import retrofit2.http.FieldMap;
import rx.Observable;

public interface Repository {

    Observable<ResponseBody> downloadFile(String url);

    Observable<Response<ResourceBean>> resourceInfo();

    Observable<Response<UserBean>> getUserInfo(Map<String, String> startInfo);

    Observable<BaseResponse> getVerificationCode(String number);

    Observable<BaseResponse> verifyCode(String number, String code);

    Observable<BaseResponse> submitUserInfo(String token, Map<String, String> userInfo);

    Observable<Response<List<LessonBean>>> getLessonList();

    Observable<Response<LessonPackBean>> getLessonPack(String id);

    Observable<Response<CenterTimeBean>> getCenterTime(String id);

    Observable<BaseResponse> bookCenter(Map<String, String> info);

    Observable<Response<List<BookInfoBean>>> getBookInfo();

    Observable<BaseResponse> savePhrase(Map<String, Object> phrases);

}
