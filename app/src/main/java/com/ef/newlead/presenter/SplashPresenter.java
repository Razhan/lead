package com.ef.newlead.presenter;

import android.content.Context;

import com.ef.newlead.Constant;
import com.ef.newlead.data.model.DataBean.ResourceBean;
import com.ef.newlead.data.model.DataBean.UserBean;
import com.ef.newlead.data.model.Response;
import com.ef.newlead.domain.usecase.UseCase;
import com.ef.newlead.ui.view.SplashView;
import com.ef.newlead.util.FileUtils;
import com.ef.newlead.util.SharedPreUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.ResponseBody;

public class SplashPresenter extends Presenter<SplashView> {

    private static final int DEFAULT_INIT_STEP = 2;
    private AtomicInteger stepCount = new AtomicInteger(0);

    public SplashPresenter(Context context, SplashView view, UseCase useCase) {
        super(context, view, useCase);
    }

    public void init() {
        if (!SharedPreUtils.contain(Constant.USER_ID)) {
            getUserInfo();
        } else {
            stepCount.incrementAndGet();
        }

        getResourceInfo();
    }

    private void InitCompleted() {
        if (stepCount.incrementAndGet() == DEFAULT_INIT_STEP) {
            getView().afterInit();
        }
    }

    private void getResourceInfo() {
        useCase.new Builder<Response<ResourceBean>>()
                .useCaseName("ResourceInfo")
                .onSuccess(response -> {
                    if (!SharedPreUtils.getString(Constant.RESOURCE_HASH, "")
                            .equals(response.getData().getHash())) {
                        downloadResourceFile(context, response.getData().getSrc(), response.getData().getHash());
                    } else {
                        InitCompleted();
                    }
                })
                .build();
    }

    private void getUserInfo() {
        Map<String, String> startInfo = new HashMap<>();
        startInfo.put("device", "android");
        startInfo.put("campaign", "");
        startInfo.put("source", "");
        startInfo.put("appstore", "");

        useCase.new Builder<Response<UserBean>>()
                .useCaseArgs(startInfo)
                .onSuccess(user -> {
                    SharedPreUtils.putString(Constant.USER_ID, user.getData().getId());
                    SharedPreUtils.putString(Constant.USER_TOKEN, user.getData().getToken());
                    SharedPreUtils.putString(Constant.USER_RULE, user.getData().getRule());

                    InitCompleted();
                })
                .build();
    }

    private void downloadResourceFile(Context context, String url, String hash) {
        useCase.new Builder<ResponseBody>()
                .useCaseArgs(url)
                .onSuccess(responseBody -> {
                    if (saveFile(context, responseBody)) {
                        SharedPreUtils.putString(Constant.RESOURCE_HASH, hash);
                    }
                    InitCompleted();
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
