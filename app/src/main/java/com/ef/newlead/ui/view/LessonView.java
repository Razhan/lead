package com.ef.newlead.ui.view;

import com.ef.newlead.data.model.DataBean.LessonBean;

import java.util.List;

public interface LessonView extends View {

    void renderLessonList(List<LessonBean> list);

}
