package com.ef.newlead.presenter;

import android.content.Context;
import android.net.Uri;

import com.ef.newlead.data.model.ActivityTemplate;
import com.ef.newlead.data.model.Lesson;
import com.ef.newlead.domain.usecase.UseCase;
import com.ef.newlead.ui.view.VideoView;
import com.ef.newlead.util.FileUtils;
import com.google.android.exoplayer.util.Assertions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VideoPresenter extends Presenter<VideoView> {

    protected ActivityTemplate mActivityTemplate;

    public VideoPresenter(Context context, VideoView view, UseCase useCase) {
        super(context, view, useCase);
    }

    @Override
    public void onCreate() {
        getVideoPack("");

        loadSource();
    }

    public void getVideoPack(String id) {
        Observable.zip(
                Observable.range(1, 101),
                Observable.interval(20, TimeUnit.MILLISECONDS),
                (obs, timer) -> obs
        )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    if (item <= 100) {
                        getView().updateLoadProgress(item);
                    } else {
                        getView().afterLoaded();
                    }
                });
    }

    public void submitScore() {

    }

    private void loadSource() {
        Lesson lesson = FileUtils.readObjectFromAssertFile(context, "airport.json", Lesson.class);
        List<ActivityTemplate> templates = lesson.getActivities();

        ActivityTemplate template = null;

        for (ActivityTemplate at : templates) {
            if (isTarget(at)) {
                template = at;
                break;
            }
        }

        Assertions.checkNotNull(template);

        mActivityTemplate = template;
    }

    protected boolean isTarget(ActivityTemplate at) {
        return ActivityTemplate.TemplateType.Dialog.equals(at.getTypeNum());
    }

    public Uri getVideo() {
        String path = "file:///android_asset/";
        return Uri.parse(path + mActivityTemplate.getVideo().getMedia());
    }

    public double getVideoRatio() {
        return mActivityTemplate.getVideo().getRatio();
    }

    public int getDialogSize() {
        return mActivityTemplate.getDialogs().size();
    }

    public List<ActivityTemplate.DialogBean> getDialogsByIndex(int index) {
        return mActivityTemplate.getDialogs().get(index);
    }

    public List<ActivityTemplate.DialogBean> getAllDialogueBeans() {
        ArrayList<ActivityTemplate.DialogBean> allBeans = new ArrayList<>();

        List<List<ActivityTemplate.DialogBean>> dialogs = mActivityTemplate.getDialogs();
        for (List<ActivityTemplate.DialogBean> beans : dialogs) {
            if (beans == null) {
                break;
            }

            for (ActivityTemplate.DialogBean bean : beans) {
                allBeans.add(bean);
            }
        }

        return allBeans;
    }

    public List<Double> getTimeStamps() {
        List<Double> timestamps = new ArrayList<>();

        for (List<ActivityTemplate.DialogBean> beans : mActivityTemplate.getDialogs()) {
            if (beans == null) {
                break;
            }

            for (ActivityTemplate.DialogBean bean : beans) {
                timestamps.add(bean.getStartTime());
            }
        }

        return timestamps;
    }
}
