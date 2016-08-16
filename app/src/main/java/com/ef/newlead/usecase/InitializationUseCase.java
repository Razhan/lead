package com.ef.newlead.usecase;

import com.ef.newlead.data.model.ResourceResponse;
import com.ef.newlead.data.repostory.RestfulDataSource;
import com.ef.newlead.util.FileUtils;

import okhttp3.ResponseBody;
import rx.Observable;

public class InitializationUseCase extends UseCase {

    public Observable<Boolean> unzip(String targetDirectory, String zipFile) {
        return Observable.create(subscriber -> {
                    try {
                        FileUtils.unzip(targetDirectory, zipFile);
                        subscriber.onNext(true);
                        subscriber.onCompleted();
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }
        );
    }

    @UseCaseMethod(name = "Download")
    public Observable<ResponseBody> getResourceFile(String url) {
        return RestfulDataSource.getInstance().downloadFile(url);
    }

    @UseCaseMethod(name = "ResourceInfo")
    public Observable<ResourceResponse> getResourceInfo() {
        return RestfulDataSource.getInstance().resourceInfo();
    }

}
