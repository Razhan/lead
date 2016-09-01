package com.ef.newlead.presenter;

import android.content.Context;

import com.ef.newlead.domain.usecase.UseCase;
import com.ef.newlead.ui.view.VideoView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VideoPresenter extends Presenter<VideoView> {

    public VideoPresenter(Context context, VideoView view, UseCase useCase) {
        super(context, view, useCase);
    }

    @Override
    public void onCreate() {
        getVideoPack("");
    }

    public void getVideoPack(String id) {
        Observable.zip(
                    Observable.range(1, 100),
                    Observable.interval(20, TimeUnit.MILLISECONDS),
                    (obs, timer) -> obs
                )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    if (item < 100) {
                        getView().updateLoadProgress(item);
                    } else {
                        getView().afterLoaded();
                    }
                });
    }

    public void submitScore() {

    }

}
