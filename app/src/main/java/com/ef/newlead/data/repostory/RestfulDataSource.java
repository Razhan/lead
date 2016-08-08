package com.ef.newlead.data.repostory;

import com.ef.newlead.util.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class RestfulDataSource implements Repository {

    private final static RestfulDataSource dataSource = new RestfulDataSource();
    private final NewLeadService service;
    private final static int CONNECTION_TIMEOUT = 10;

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
                .baseUrl(Constant.END_POINT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        service = retrofit.create(NewLeadService.class);
    }

    public static RestfulDataSource getInstance() {
        return dataSource;
    }

    @Override
    public Observable<ResponseBody> downloadFile() {
        return service.downloadFile();
    }

}
