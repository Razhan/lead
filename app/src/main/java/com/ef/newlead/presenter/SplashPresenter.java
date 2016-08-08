package com.ef.newlead.presenter;

import android.content.Context;

import com.ef.newlead.ui.view.SplashView;
import com.ef.newlead.usecase.UseCase;
import com.ef.newlead.util.Constant;
import com.ef.newlead.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;

import okhttp3.ResponseBody;

public class SplashPresenter extends Presenter<SplashView> {

    public SplashPresenter(SplashView view, UseCase useCase) {
        super(view, useCase);
    }

    public void downloadResourceFile(Context context) {
        useCase.new Builder<ResponseBody>()
                .useCaseFunction("download")
                .onSuccess(responseBody -> saveFile(context, responseBody))
                .build();
    }

    private boolean saveFile(Context context, ResponseBody responseBody) {
        String path = FileUtils.getInternalFolderPath(context, null);
        File file = new File(path + Constant.RESOURCE_ZIP_FILE_NAME);

        try {
            if (file.exists()) {
                file.delete();
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

}
