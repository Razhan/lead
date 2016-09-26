package com.ef.newlead.presenter;

import android.content.Context;

import com.ef.newlead.data.model.DataBean.BaseResponse;
import com.ef.newlead.domain.usecase.PhraseBookUseCase;
import com.ef.newlead.ui.view.PhraseBookView;
import com.ef.newlead.ui.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhraseBookPresenter extends Presenter<PhraseBookView> {

    public PhraseBookPresenter(Context context, PhraseBookView view) {
        super(context, view, new PhraseBookUseCase());
    }

    public void getLessonList(String lessonId, String activityId,
                              String lessonHash, List<String> phrase) {
        Map<String, Object> map = new HashMap<>();

        map.put("lesson_id", lessonId);
        map.put("activity_id", activityId);
        map.put("lesson_content_hash", lessonHash);
        map.put("phrases", phrase);

        useCase.new Builder<BaseResponse>()
                .useCaseArgs(map)
                .onSuccess(response -> getView().afterSaved())
                .build();
    }
}
