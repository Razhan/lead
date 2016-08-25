package com.ef.newlead.presenter;

import android.content.Context;

import com.ef.newlead.ErrorHandler;
import com.ef.newlead.data.model.BaseResponse;
import com.ef.newlead.domain.usecase.UseCase;
import com.ef.newlead.ui.view.VerificationView;

public class VerificationPresenter extends Presenter<VerificationView> {

    public VerificationPresenter(Context context, VerificationView view, UseCase useCase) {
        super(context, view, useCase);
    }

    public void getVerificationCode(String number) {
        useCase.new Builder<BaseResponse>()
                .useCaseArgs(number.replaceAll("\\s", ""))
                .onSuccess(response -> getView().afterNumberSubmit(true))
                .onError(exception -> {
                    ErrorHandler.showError(exception);
                    getView().afterNumberSubmit(false);
                })
                .build();
    }

    public void VerifyCode(String number, String code) {
        useCase.new Builder<BaseResponse>()
                .useCaseArgs(number.replaceAll("\\s", ""), code)
                .onSuccess(response -> getView().afterCodeVerified(true))
                .onError(exception -> getView().afterCodeVerified(false))
                .build();
    }

}
