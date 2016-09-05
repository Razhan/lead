package com.ef.newlead.data.repostory;

import com.ef.newlead.data.model.DataBean.BaseResponse;
import com.ef.newlead.data.model.DataBean.ResourceBean;
import com.ef.newlead.data.model.DataBean.Response;
import com.ef.newlead.data.model.DataBean.UserBean;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
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

}
