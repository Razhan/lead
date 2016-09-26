package com.ef.newlead.presenter;

import android.content.Context;
import android.util.Log;

import com.ef.newlead.data.model.DataBean.LessonBean;
import com.ef.newlead.data.model.DataBean.LessonPackBean;
import com.ef.newlead.data.model.DataBean.Response;
import com.ef.newlead.domain.usecase.LessonUseCase;
import com.ef.newlead.domain.usecase.UseCase;
import com.ef.newlead.ui.view.LessonView;

import java.util.List;

public class LessonPresenter extends Presenter<LessonView> {

    public LessonPresenter(Context context, LessonView view) {
        super(context, view, new LessonUseCase());
    }

    public void getLessonList() {
        useCase.new Builder<Response<List<LessonBean>>>()
                .useCaseName("GetLessonList")
                .onSuccess(list -> getView().renderLessonList(list.getData()))
                .build();
    }

    public void getLessonPack(String id) {
        useCase.new Builder<Response<LessonPackBean>>()
                .useCaseArgs(id)
                .onSuccess(pack -> {
                    Log.d("pack", pack.getData().getId());
                })
                .build();
    }




}
