package com.ef.newlead.data.repostory;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import rx.Observable;

public interface NewLeadService {

    @GET("/test/resource.zip")
    Observable<ResponseBody> downloadFile();

}
