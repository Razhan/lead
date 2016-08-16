package com.ef.newlead.presenter;

import android.content.Context;

import com.ef.newlead.Constant;
import com.ef.newlead.data.model.ResourceResponse;
import com.ef.newlead.ui.view.SplashView;
import com.ef.newlead.usecase.UseCase;
import com.ef.newlead.util.FileUtils;
import com.ef.newlead.util.SharedPreUtils;

import java.io.IOException;

import okhttp3.ResponseBody;

public class SplashPresenter extends Presenter<SplashView> {

    public SplashPresenter(SplashView view, UseCase useCase) {
        super(view, useCase);
    }

    private void downloadResourceFile(Context context, String url, String hash) {
        useCase.new Builder<ResponseBody>()
                .useCaseArgs(url)
                .onSuccess(responseBody -> {
                    if (saveFile(context, responseBody)) {
                        SharedPreUtils.putString(Constant.RESOURCE_HASH, hash);
                    }
                })
                .build();
    }

    public void getResourceInfo(Context context) {
        useCase.new Builder<ResourceResponse>()
                .useCaseMethod("ResourceInfo")
                .onSuccess(response -> {
                    if (!SharedPreUtils.contain(Constant.RESOURCE_HASH) || !SharedPreUtils.getString(Constant.RESOURCE_HASH, "")
                            .equals(response.getData().getHash())) {
                        downloadResourceFile(context, response.getData().getSrc(), response.getData().getHash());
                    }
                })
                .build();
    }

    private boolean saveFile(Context context, ResponseBody responseBody) {
        boolean result;
        try {
            result = FileUtils.saveToInternalStorage(context, responseBody.bytes(),
                    Constant.RESOURCE_ZIP_FILE_NAME);

        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }

        return result;
    }

}
