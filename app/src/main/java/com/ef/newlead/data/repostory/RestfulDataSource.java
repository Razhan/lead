package com.ef.newlead.data.repostory;

import com.ef.newlead.Constant;
import com.ef.newlead.data.model.DataBean.Response;
import com.ef.newlead.data.model.DataBean.BaseResponse;
import com.ef.newlead.data.model.DataBean.CenterTimeBean;
import com.ef.newlead.data.model.DataBean.LessonBean;
import com.ef.newlead.data.model.DataBean.LessonPackBean;
import com.ef.newlead.data.model.DataBean.ResourceBean;
import com.ef.newlead.data.model.DataBean.UserBean;
import com.ef.newlead.ui.widget.DownloadProgressInterceptor;
import com.ef.newlead.util.SharedPreUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import timber.log.Timber;

public class RestfulDataSource implements Repository {

    private final static int CONNECTION_TIMEOUT = 30;
    private final NewLeadService restfulService;

    public RestfulDataSource() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor((message) -> Timber.tag("OKHttp").d(message))
                            .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("user_id", SharedPreUtils.getString(Constant.USER_ID, ""))
                            .header("app_version", SharedPreUtils.getString(Constant.APP_VERSION, "1.0"))
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                })
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

    @Override
    public Observable<Response<List<LessonBean>>> getLessonList() {
        return restfulService.getLessonList();
    }

    @Override
    public Observable<Response<LessonPackBean>> getLessonPack(String id) {
        return restfulService.getLessonPackage(id);
    }

    @Override
    public Observable<Response<CenterTimeBean>> getCenterTime(String id) {
        return restfulService.getCenterTime(id);
    }

    @Override
    public Observable<BaseResponse> bookCenter(Map<String, String> info) {
        return restfulService.bookCenter(info);
    }
}
