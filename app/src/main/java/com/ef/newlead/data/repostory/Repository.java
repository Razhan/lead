package com.ef.newlead.data.repostory;


import okhttp3.ResponseBody;
import rx.Observable;

public interface Repository {

    Observable<ResponseBody> downloadFile();

}
