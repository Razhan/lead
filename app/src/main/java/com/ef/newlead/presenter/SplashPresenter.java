package com.ef.newlead.presenter;

import android.content.Context;

import com.ef.newlead.Constant;
import com.ef.newlead.ui.view.SplashView;
import com.ef.newlead.usecase.UseCase;
import com.ef.newlead.util.FileUtils;

import java.io.IOException;

import okhttp3.ResponseBody;

public class SplashPresenter extends Presenter<SplashView> {

    public SplashPresenter(SplashView view, UseCase useCase) {
        super(view, useCase);
    }

    public void downloadResourceFile(Context context) {
        useCase.new Builder<ResponseBody>()
                .useCaseMethod("download")
                .onSuccess(responseBody -> saveFile(context, responseBody))
                .build();
    }

    private boolean saveFile(Context context, ResponseBody responseBody) {
        boolean result;
        try {
            /***
             * FIXME: We'd better have a isolated persistence part instead of using a simple util
             * tool here. Also it's not the presenter's responsibility to persist things.
             *
             */
            result = FileUtils.saveToInternalStorage(context, responseBody.bytes(),
                    Constant.RESOURCE_ZIP_FILE_NAME);

        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

}
