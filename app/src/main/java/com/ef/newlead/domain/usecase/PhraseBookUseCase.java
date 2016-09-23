package com.ef.newlead.domain.usecase;

import com.ef.newlead.data.model.DataBean.BaseResponse;

import java.util.Map;

import rx.Observable;

public class PhraseBookUseCase extends UseCase {

    @UseCaseMethod
    public Observable<BaseResponse> savePhrase(Map<String, Object> map) {
        return repository.savePhrase(map);
    }
}
