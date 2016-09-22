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
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

public interface NewLeadService {

    @GET
    Observable<ResponseBody> downloadFile(@Url String url);

    @GET("/api/leadgen/appres/package")
    Observable<Response<ResourceBean>> resourceInfo();

    @FormUrlEncoded
    @POST("/api/leadgen/user/getstarted")
    Observable<Response<UserBean>> getUserInfo(@FieldMap Map<String, String> startInfo);

    @FormUrlEncoded
    @POST("/api/leadgen/sms/send")
    Observable<BaseResponse> getVerificationCode(@Field("phone") String number);

    @FormUrlEncoded
    @POST("/api/leadgen/sms/verify")
    Observable<BaseResponse> verifyCode(@Field("phone") String number,
                                        @Field("code") String code);

    @FormUrlEncoded
    @POST("/api/leadgen/user/profile")
    Observable<BaseResponse> submitUserInfo(@Header("Authorization") String token,
                                            @FieldMap Map<String, String> info);

    @POST("/api/leadgen/lesson/list")
    Observable<Response<List<LessonBean>>> getLessonList();

    @FormUrlEncoded
    @POST("/api/leadgen/lesson/package/Query")
    Observable<Response<LessonPackBean>> getLessonPackage(@Field("lesson_id") String id);

    @FormUrlEncoded
    @POST("/api/leadgen/center/timeslots")
    Observable<Response<CenterTimeBean>> getCenterTime(@Field("center_id") String id);

    @FormUrlEncoded
    @POST("/api/leadgen/center/book")
    Observable<BaseResponse> bookCenter(@FieldMap Map<String, String> info);

    @POST("/api/leadgen/center/bookinginfo")
    Observable<Response<List<BookInfoBean>>> getBookInfo();

}
