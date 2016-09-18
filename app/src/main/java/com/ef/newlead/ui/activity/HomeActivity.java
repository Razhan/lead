package com.ef.newlead.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ef.newlead.R;
import com.ef.newlead.data.model.DataBean.LessonBean;
import com.ef.newlead.domain.usecase.LessonUseCase;
import com.ef.newlead.presenter.LessonPresenter;
import com.ef.newlead.ui.view.LessonView;

import java.util.List;

import butterknife.OnClick;

public class HomeActivity extends BaseMVPActivity<LessonPresenter> implements LessonView {

    private List<LessonBean> lessonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        translucentStatusBar = true;
        super.onCreate(savedInstanceState);

        presenter.getLessonList();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_home;
    }

    @NonNull
    @Override
    protected LessonPresenter createPresenter() {
        return new LessonPresenter(this, this, new LessonUseCase());
    }

    @OnClick(R.id.home_top_view)
    public void onClick() {
        if (lessonList != null) {
            Intent i = new Intent(this, DialogueVideoActivity.class);
            i.putExtra(DialogueVideoActivity.KEY_LESSONID, lessonList.get(0).getId());
            startActivity(i);
        }
    }

    @Override
    public void renderLessonList(List<LessonBean> list) {
        lessonList = list;
    }

}
