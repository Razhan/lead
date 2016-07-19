package com.ef.cat.data.repostory;

import com.ef.cat.Constant;
import com.ef.cat.data.model.News;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;

public interface RestfulService {

    @GET("Appdata/NewsList/?newstype=1&isline=0&size=20&page=1&type=1&id=homepage")
    Observable<News> getNews();

    @GET("/maven2/com/squareup/retrofit/retrofit/2.0.0-beta2/retrofit-2.0.0-beta2-javadoc.jar")
    Observable<ResponseBody> downloadFile();

    class Creator {

        public static RestfulService service() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.END_POINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            return retrofit.create(RestfulService.class);
        }
    }

}
