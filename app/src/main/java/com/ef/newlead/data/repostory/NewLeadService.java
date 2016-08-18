package com.ef.newlead.data.repostory;

import com.ef.newlead.data.model.DataBean.ResourceBean;
import com.ef.newlead.data.model.DataBean.UserBean;
import com.ef.newlead.data.model.Response;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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
    Observable<Response<UserBean>> getUserInfo(
            @Field("device") String device,
            @Field("campaign") String campaign,
            @Field("source") String source,
            @Field("appstore") String appStore
    );

}
