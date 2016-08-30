package com.ef.newlead.domain.usecase;

import com.ef.newlead.data.model.DataBean.BaseResponse;

import rx.Observable;

public class VerificationUseCase extends UseCase {

    @UseCaseMethod
    public Observable<BaseResponse> getVerificationCode(String number) {
        return Observable.empty();
    }

    @UseCaseMethod
    public Observable<BaseResponse> verifyCode(String number, String code) {
        return repository.verifyCode(number, code);
    }


}
