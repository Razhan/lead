package com.ef.newlead.data.repostory;

import com.ef.newlead.data.model.ResourceResponse;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface NewLeadService {

    @GET
    Observable<ResponseBody> downloadFile(@Url String url);


    @GET("/api/leadgen/appres/package")
    Observable<ResourceResponse> resourceInfo();

}
