package com.ef.newlead.data.repostory;

import com.ef.newlead.Constant;
import com.ef.newlead.data.model.DataBean.BaseResponse;
import com.ef.newlead.data.model.DataBean.ResourceBean;
import com.ef.newlead.data.model.DataBean.Response;
import com.ef.newlead.data.model.DataBean.UserBean;
import com.ef.newlead.ui.widget.DownloadProgressInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class RestfulDataSource implements Repository {

    private final static int CONNECTION_TIMEOUT = 30;
    private final NewLeadService restfulService;

    public RestfulDataSource() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(new DownloadProgressInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_END_POINT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        restfulService = retrofit.create(NewLeadService.class);
    }

    @Override
    public Observable<ResponseBody> downloadFile(String url) {
        return restfulService.downloadFile(url);
    }

    @Override
    public Observable<Response<ResourceBean>> resourceInfo() {
        return restfulService.resourceInfo();
    }

    @Override
    public Observable<Response<UserBean>> getUserInfo(Map<String, String> startInfo) {
        return restfulService.getUserInfo(startInfo);
    }

    @Override
    public Observable<BaseResponse> getVerificationCode(String number) {
        return restfulService.getVerificationCode(number);
    }

    @Override
    public Observable<BaseResponse> verifyCode(String number, String code) {
        return restfulService.verifyCode(number, code);
    }

    @Override
    public Observable<BaseResponse> submitUserInfo(String token, Map<String, String> userInfo) {
        return restfulService.submitUserInfo(token, userInfo);
    }
}
