package com.ef.newlead.domain.usecase;

import com.ef.newlead.data.model.DataBean.LessonBean;
import com.ef.newlead.data.model.DataBean.LessonPackBean;
import com.ef.newlead.data.model.DataBean.Response;

import java.util.List;

import rx.Observable;

public class LessonUseCase extends UseCase {

    @UseCaseMethod(name = "GetLessonList")
    public Observable<Response<List<LessonBean>>> getLessonList() {
        return repository.getLessonList();
    }

    @UseCaseMethod
    public Observable<Response<LessonPackBean>> getLessonPackage(String id) {
        return repository.getLessonPack(id);
    }

}
