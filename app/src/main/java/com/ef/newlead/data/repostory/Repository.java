package com.ef.newlead.data.repostory;


import com.ef.newlead.data.model.ResourceResponse;

import okhttp3.ResponseBody;
import rx.Observable;

public interface Repository {

    Observable<ResponseBody> downloadFile(String url);

    Observable<ResourceResponse> resourceInfo();
}
