package com.ef.newlead.domain.usecase;

import com.ef.newlead.data.model.DataBean.BaseResponse;

import java.util.Map;

import rx.Observable;

public class CollectInfoUseCase extends UseCase {

    @UseCaseMethod
    public Observable<BaseResponse> submitUserInfo(String token, Map<String, String> userInfo) {
        return repository.submitUserInfo(token, userInfo);
    }

}
