package com.ef.newlead.presenter;

import android.content.Context;

import com.ef.newlead.Constant;
import com.ef.newlead.data.model.DataBean.BaseResponse;
import com.ef.newlead.domain.usecase.UseCase;
import com.ef.newlead.ui.view.CollectInfoView;
import com.ef.newlead.util.SharedPreUtils;

import java.util.HashMap;
import java.util.Map;

public class CollectInfoPresenter extends Presenter<CollectInfoView> {

    public CollectInfoPresenter(Context context, CollectInfoView view, UseCase useCase) {
        super(context, view, useCase);
    }

    public void submitInfo() {
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("age", SharedPreUtils.getString(Constant.USER_AGE_INDEX, ""));
        userInfo.put("purpose", SharedPreUtils.getString(Constant.USER_PURPOSE, ""));
        userInfo.put("level", SharedPreUtils.getString(Constant.USER_LEVEL, ""));
        userInfo.put("city", SharedPreUtils.getString(Constant.USER_CITY, ""));
        userInfo.put("phone", SharedPreUtils.getString(Constant.USER_PHONE, ""));

        String token = "Basic " + SharedPreUtils.getString(Constant.USER_TOKEN, "");

        useCase.new Builder<BaseResponse>()
                .useCaseArgs(token, userInfo)
                .onSuccess(response -> getView().afterSubmitInfo())
                .build();
    }

}
