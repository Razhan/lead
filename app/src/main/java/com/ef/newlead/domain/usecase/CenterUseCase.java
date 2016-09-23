package com.ef.newlead.domain.usecase;

import com.ef.newlead.data.model.DataBean.BaseResponse;
import com.ef.newlead.data.model.DataBean.BookInfoBean;
import com.ef.newlead.data.model.DataBean.CenterTimeBean;
import com.ef.newlead.data.model.DataBean.Response;

import java.util.List;
import java.util.Map;

import rx.Observable;

public class CenterUseCase extends UseCase  {

    @UseCaseMethod
    public Observable<Response<CenterTimeBean>> getCenterTime(String id) {
        return repository.getCenterTime(id);
    }

    @UseCaseMethod
    public Observable<BaseResponse> bookCenter(Map<String, String> info) {
        return repository.bookCenter(info);
    }

    @UseCaseMethod(name = "GetBookInfo")
    public Observable<Response<List<BookInfoBean>>> getBookInfo() {
        return repository.getBookInfo();
    }


}
