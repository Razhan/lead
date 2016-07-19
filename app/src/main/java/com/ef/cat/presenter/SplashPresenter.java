package com.ef.cat.presenter;

import android.content.Context;
import android.util.Log;

import com.ef.cat.Constant;
import com.ef.cat.data.model.News;
import com.ef.cat.view.SplashView;
import com.ran.delta.domain.usecase.UseCase;
import com.ran.delta.presentation.presenter.MvpBasePresenter;
import com.ran.delta.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.ResponseBody;

public class SplashPresenter extends MvpBasePresenter<SplashView> {

    private final UseCase useCase;

    @Inject
    public SplashPresenter(@Named("Initialization") UseCase useCase) {
        this.useCase = useCase;
    }

    public void getNews() {
        useCase.new Builder<News>()
                .useCaseFunction("getNews")
                .onSuccess(news -> {
                    news.getCount();
                })
                .onError(e -> {
                    e.printStackTrace();
                })
                .build();
    }

    public void unzip(Context context) {
        useCase.new Builder<Boolean>()
                .useCaseArgs(context, FileUtils.getInternalFolderPath(context, null), Constant.RESOURCE_ZIP_FILE_NAME)
                .onSuccess(bool -> {
                    Log.d("", "");
                })
                .onError(e -> {
                    e.printStackTrace();
                })
                .build();
    }

    public void downloadResourceFile(Context context) {
        useCase.new Builder<ResponseBody>()
                .useCaseFunction("download")
                .onSuccess(responseBody -> {
                    saveFile(context, responseBody);
                })
                .onError(e -> {
                    e.printStackTrace();
                })
                .build();
    }

    private boolean saveFile(Context context, ResponseBody responseBody) {
        String path = FileUtils.getInternalFolderPath(context, null);
        File file = new File(path + Constant.RESOURCE_ZIP_FILE_NAME);

        try {
            if (!(file.exists() && file.delete())) {
                return false;
            }

            if (file.createNewFile()) {
                return false;
            }

            FileOutputStream stream = new FileOutputStream(file);
            byte[] buf = responseBody.bytes();
            stream.write(buf);
            stream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void unsubscribe() {
        useCase.unsubscribe();
    }
}
