package com.ef.newlead.usecase;

import com.ef.newlead.data.repostory.RestfulDataSource;
import com.ef.newlead.util.FileUtils;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;

public class InitializationUseCase extends UseCase {

    public Observable<Boolean> unzip(String targetDirectory, String zipFile) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    FileUtils.unzip(targetDirectory, zipFile);
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @UseCaseMethod(name = "download")
    public Observable<ResponseBody> getResourceFile() {
        return RestfulDataSource.getInstance().downloadFile();
    }

}
