package com.ef.newlead.data.repostory;

import com.ef.newlead.Constant;
import com.ef.newlead.data.model.ResourceResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class RestfulDataSource implements Repository {

    private final static int CONNECTION_TIMEOUT = 10;
    private static RestfulDataSource dataSource;
    private final NewLeadService restService;

    private RestfulDataSource() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("type", "android")
                            .build();
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_END_POINT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        restService = retrofit.create(NewLeadService.class);
    }

    public static RestfulDataSource getInstance() {
        if (dataSource == null) {
            dataSource = new RestfulDataSource();
        }

        return dataSource;
    }

    @Override
    public Observable<ResponseBody> downloadFile(String url) {
        return restService.downloadFile(url);
    }

    @Override
    public Observable<ResourceResponse> resourceInfo() {
        return restService.resourceInfo();
    }
}
