package com.ef.cat.usecase;

import android.content.Context;

import com.ef.cat.data.repostory.CatRepository;
import com.ran.delta.domain.annotation.UseCaseFunction;
import com.ran.delta.domain.usecase.UseCase;
import com.ran.delta.utils.FileUtils;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;

public class InitializationUseCase extends UseCase {

    private CatRepository catRepository;

    @Inject
    public InitializationUseCase(CatRepository repository) {
        this.catRepository = repository;
    }

    @UseCaseFunction(name = "unzip")
    public Observable<Boolean> unzip(Context context, String targetDirectory, String zipFile) {
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

    @UseCaseFunction(name = "download")
    public Observable<ResponseBody> getResourceFile() {
        return catRepository.getRestfulService().downloadFile();
    }

}
